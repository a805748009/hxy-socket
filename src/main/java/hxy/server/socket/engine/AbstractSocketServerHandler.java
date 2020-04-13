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

    private static HandlerExceptionAdvice handlerExceptionAdvice;

    protected static SocketMsgHandler socketMsgHandler = SpringApplicationContextHolder.getBean("socketMsgHandler");

    static{
        if(SpringApplicationContextHolder.containsBean("ExceptionHandler")){
            handlerExceptionAdvice = SpringApplicationContextHolder.getBean("ExceptionHandler");
        }else {
            handlerExceptionAdvice = null;
        }
    }

    void doHandler(Runnable runnable, ChannelHandlerContext ctx) {
        CompletableFuture.runAsync(runnable, ctx.executor())
                .exceptionally(e -> {
                    exceptionCaught(ctx, e);
                    return null;
                });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (handlerExceptionAdvice != null) {
            handlerExceptionAdvice.doException(ctx, cause);
        } else {
            cause.printStackTrace();
        }
    }
}
