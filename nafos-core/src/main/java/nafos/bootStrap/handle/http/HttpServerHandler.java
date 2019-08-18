package nafos.bootStrap.handle.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import nafos.core.util.NettyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * @author huangxinyu
 * @version 创建时间：2018年1月4日 上午11:51:28
 * 连接处理类
 */
@ChannelHandler.Sharable
@Component
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private static final Set<HttpMethod> allowedMethods = new HashSet(Arrays.asList(GET, POST, PUT, HEAD, DELETE, PATCH));


    @Autowired
    HttpExecutorPoolChoose httpExecutorPoolChoose;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof FullHttpRequest)) {
            NettyUtil.sendError(ctx, BAD_REQUEST);
            return;
        }

        FullHttpRequest request = (FullHttpRequest) msg;

        // 1.校验request合法性
        if (!checkRequest(ctx, request)) return;


        // 2.选择处理方案
        httpExecutorPoolChoose.choosePool(ctx, request);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn(cause.toString());
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
        if (!allowedMethods.contains(request.method())) {
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


}
