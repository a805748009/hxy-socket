package nafos.core.helper;

import io.netty.channel.ChannelHandlerContext;
import nafos.core.entry.ClassAndMethod;
import nafos.core.entry.ResultStatus;
import nafos.core.entry.error.BizException;
import nafos.core.util.NettyUtil;
import nafos.core.util.ObjectUtil;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 上午11:53
 * @Description TODO
 **/
public class ClassAndMethodHelper {

    public static boolean checkResultStatus(ClassAndMethod filter, ChannelHandlerContext ctx, Object object, String param) {
        if (ObjectUtil.isNotNull(filter)) {
            ResultStatus resultStatus = (ResultStatus) filter.getMethod().invoke(
                    SpringApplicationContextHolder.getSpringBeanForClass(filter.getClazz()), filter.getIndex(), ctx, object, param);
            if (!resultStatus.isSuccess()) {
                NettyUtil.sendError(ctx, resultStatus.getBizException());
                return false;
            }
        }
        return true;
    }

}
