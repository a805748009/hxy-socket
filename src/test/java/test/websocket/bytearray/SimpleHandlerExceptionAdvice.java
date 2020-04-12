package test.websocket.bytearray;

import hxy.server.socket.anno.ExceptionHandler;
import hxy.server.socket.engine.HandlerExceptionAdvice;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Description 全局异常处理
 * @Author xinyu.huang
 * @Time 2020/4/11 21:27
 */
@ExceptionHandler
public class SimpleHandlerExceptionAdvice implements HandlerExceptionAdvice {
    @Override
    public void doException(ChannelHandlerContext ctx,Throwable e) {
        e.printStackTrace();
    }
}
