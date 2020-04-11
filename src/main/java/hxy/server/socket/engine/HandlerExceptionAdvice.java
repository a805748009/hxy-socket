package hxy.server.socket.engine;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description 全局异常处理
 * @Author xinyu.huang
 * @Time 2020/4/11 20:25
 */
public interface HandlerExceptionAdvice {
    void doException(ChannelHandlerContext ctx,Throwable e);
}
