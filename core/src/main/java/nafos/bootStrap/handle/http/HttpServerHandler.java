package nafos.bootStrap.handle.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import nafos.core.util.NettyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * @author huangxinyu
 * @version 创建时间：2018年1月4日 上午11:51:28
 * 连接处理类
 */
@ChannelHandler.Sharable
@Component
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    @Autowired
    HttpExecutorPoolChoose httpExecutorPoolChoose;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpRequest)) {
            NettyUtil.sendError(ctx, BAD_REQUEST);
            return;
        }

        FullHttpRequest request = (FullHttpRequest) msg;

        // 1.校验request合法性
        if (!checkRequest(ctx, request)) return;


        // 2.选择处理方案
        httpFilter(ctx, request);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.toString());
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 校验request合法性
     *
     * @param ctx
     * @param request
     */
    private boolean checkRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess()) {
            NettyUtil.sendError(ctx, BAD_REQUEST);
            return false;
        }

        //1）跨域方法之前会先收到OPTIONS方法，直接确认
        if (request.method() == OPTIONS) {
            NettyUtil.sendOptions(ctx, OK);
            return false;
        }

        // 2)确保方法是我们需要的(目前只支持GET | POST  ,其它不支持)
        if (request.method() != GET && request.method() != POST) {
            NettyUtil.sendError(ctx, METHOD_NOT_ALLOWED);
            return false;
        }

        // 3)uri是有长度的
        final String uri = request.uri();
        if (uri == null || uri.trim().length() == 0) {
            NettyUtil.sendError(ctx, FORBIDDEN);
            return false;
        }
        return true;
    }


    /**
     * http前置filter处理
     *
     * @param ctx
     * @param request
     */
    private void httpFilter(ChannelHandlerContext ctx, FullHttpRequest request) {

        // 1.选择线程池执行
        httpExecutorPoolChoose.choosePool(ctx, request);


    }

}
