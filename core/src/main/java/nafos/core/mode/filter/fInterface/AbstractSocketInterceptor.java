package nafos.core.mode.filter.fInterface;

import io.netty.channel.ChannelHandlerContext;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.core.entry.ResultStatus;

public abstract class AbstractSocketInterceptor implements InterceptorInterface {

    @Override
    public ResultStatus interptor(ChannelHandlerContext ctx, NsRequest req) {
        return new ResultStatus(true);
    }

}
