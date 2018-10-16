package nafos.core.helper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import nafos.core.entry.ClassAndMethod;
import nafos.core.entry.ResultStatus;
import nafos.core.util.NettyUtil;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SpringApplicationContextHolder;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 上午11:53
 * @Description TODO
 **/
public class ClassAndMethodHelper {

    public static boolean httpCheckResultStatus(ClassAndMethod filter,ChannelHandlerContext ctx,FullHttpRequest object){
        if(ObjectUtil.isNotNull(filter)){
            ResultStatus resultStatus =  (ResultStatus) filter.getMethod().invoke(
                    SpringApplicationContextHolder.getSpringBeanForClass(filter.getClazz()), filter.getIndex(),ctx,object);
            if(!resultStatus.isSuccess()){
                NettyUtil.sendError(ctx, resultStatus.getResponseStatus());
                return false;
            }
        }
        return true;
    }

    public static boolean socketCheckResultStatus(ClassAndMethod filter,ChannelHandlerContext ctx,byte[] bytes){
        if(ObjectUtil.isNotNull(filter)){
            ResultStatus resultStatus =  (ResultStatus) filter.getMethod().invoke(
                    SpringApplicationContextHolder.getSpringBeanForClass(filter.getClazz()), filter.getIndex(),ctx, bytes);
            if(!resultStatus.isSuccess()){
                NettyUtil.sendError(ctx, resultStatus.getResponseStatus());
                return false;
            }
        }
        return true;
    }
}
