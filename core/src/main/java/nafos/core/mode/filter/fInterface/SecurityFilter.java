package nafos.core.mode.filter.fInterface;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import nafos.core.entry.ResultStatus;

/**
 * HTTP消息验证处理器
 */
public interface SecurityFilter {

    ResultStatus httpFilter(ChannelHandlerContext ctx, FullHttpRequest req);

    ResultStatus socketFilter(ChannelHandlerContext ctx, int code);
}
