package nafos.security.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AttributeKey;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.core.entry.ResultStatus;
import nafos.core.entry.error.BizException;
import nafos.core.mode.interceptor.InterceptorInterface;
import nafos.security.SecurityUtil;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/09/13 下午2:54
 * @Description 前置检测，刷新session存活时间
 **/
@Component
public class GlobalInterceptorFilter implements InterceptorInterface {

    @Override
    public ResultStatus interptor(ChannelHandlerContext ctx, NsRequest req) {
        ResultStatus resultStatus = new ResultStatus();
        String cookieId = req.getNafosCookieId();
        //	1)登录验证
        if(!SecurityUtil.isLogin(cookieId)){
            resultStatus.setSuccess(false);
            resultStatus.setBizException(BizException.LOGIN_SESSION_TIME_OUT);
            return resultStatus;
        }
        //	1.1)更新session存活时间
        SecurityUtil.updateSessionTime(cookieId);
        resultStatus.setSuccess(true);
        return resultStatus;
    }

    @Override
    public ResultStatus interptor(ChannelHandlerContext ctx, int code) {
        ResultStatus resultStatus = new ResultStatus();
        String cookieId = (String) ctx.channel().attr(AttributeKey.valueOf("token")).get();

        //	1)登录验证
        if(!SecurityUtil.isLogin(cookieId)){
            resultStatus.setSuccess(false);
            resultStatus.setBizException(BizException.LOGIN_SESSION_TIME_OUT);
            return resultStatus;
        }
        //	1.1)更新session存活时间
        SecurityUtil.updateSessionTime(cookieId);
        resultStatus.setSuccess(true);
        return resultStatus;
    }


}
