package nafos.core.mode.interceptor;

import io.netty.channel.ChannelHandlerContext;
import nafos.core.entry.ResultStatus;

public abstract class AbstractHttpInterceptor implements InterceptorInterface {

    @Override
    public ResultStatus interptor(ChannelHandlerContext ctx, int code,String param) {
        return new ResultStatus(true);
    }

}
