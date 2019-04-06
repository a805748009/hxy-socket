package nafos.core.mode.filter.fInterface;

import io.netty.channel.ChannelHandlerContext;
import nafos.core.entry.ResultStatus;

public abstract class AbstractHttpInterceptor implements InterceptorInterface {

    @Override
    public ResultStatus interptor(ChannelHandlerContext ctx, int code) {
        return new ResultStatus(true);
    }

}
