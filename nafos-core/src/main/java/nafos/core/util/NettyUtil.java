package nafos.core.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import nafos.core.Thread.ThreadLocalHelper;
import nafos.core.entry.error.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class NettyUtil {
    private static final Logger logger = LoggerFactory.getLogger(NettyUtil.class);

    public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        // 设置到response对象
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer(new BizException(status.code(),status.reasonPhrase()).toString(), CharsetUtil.UTF_8));
        response.headers().set("Access-Control-Allow-Origin", "*");
        response.headers().set("Access-Control-Allow-Headers", "*");
        response.headers().set("Access-Control-Allow-Credentials","true");
        response.headers().set("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
        response.headers().set(CONTENT_TYPE, "application/json;charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        ThreadLocalHelper.threadLocalRemove();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendError(ChannelHandlerContext ctx, BizException bizException) {
        // 设置到response对象
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(bizException.getStatus()),
                Unpooled.copiedBuffer(bizException.toString(), CharsetUtil.UTF_8));
        response.headers().set("Access-Control-Allow-Origin", "*");
        response.headers().set("Access-Control-Allow-Headers", "*");
        response.headers().set("Access-Control-Allow-Credentials","true");
        response.headers().set("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        ThreadLocalHelper.threadLocalRemove();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }



    public static void sendOptions(ChannelHandlerContext ctx, HttpResponseStatus status) {
        // 设置到response对象
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer("{}", CharsetUtil.UTF_8));
        //设置头部
        response.headers().set("Access-Control-Allow-Origin", "*");
        response.headers().set("Access-Control-Allow-Headers", "*");
        response.headers().set("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");

        // 发送
        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
