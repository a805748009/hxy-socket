package hxy.server.socket.engine;

import hxy.server.socket.util.SpringApplicationContextHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CompletableFuture;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/11 21:14
 */
public abstract class AbstractSocketServerHandler<T> extends SimpleChannelInboundHandler<T> {

    private static HandlerExceptionAdvice handlerExceptionAdvice = SpringApplicationContextHolder.getBean("exceptionHandler");

    protected static SocketMsgHandler socketMsgHandler = SpringApplicationContextHolder.getBean("socketMsgHandler");

    void doHandler(Runnable runnable, ChannelHandlerContext ctx) {
        CompletableFuture.runAsync(runnable, ctx.executor())
                .exceptionally(e -> {
                    exceptionCaught(ctx, e);
                    return null;
                });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        handlerExceptionAdvice.doException(ctx, cause);
    }
}
