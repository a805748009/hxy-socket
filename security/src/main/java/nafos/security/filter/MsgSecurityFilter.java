package nafos.security.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AttributeKey;
import nafos.core.Thread.ThreadLocalHelper;
import nafos.core.entry.ResultStatus;
import nafos.core.mode.filter.fInterface.SecurityFilter;
import nafos.core.util.UriUtil;
import nafos.security.SecurityUtil;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/09/13 下午2:54
 * @Description 前置消息安全监测
 **/
@Component
public class MsgSecurityFilter implements SecurityFilter {

    @Override
    public ResultStatus httpFilter(ChannelHandlerContext ctx, FullHttpRequest req) {
        ResultStatus resultStatus = new ResultStatus();
        String cookieId = ThreadLocalHelper.getRequest().getNafosCookieId();
        //	1)登录验证
        if(SecurityUtil.isNeedLogin(cookieId, UriUtil.parseUri(req.uri()))){
            resultStatus.setSuccess(false);
            resultStatus.setResponseStatus(HttpResponseStatus.FORBIDDEN);
            return resultStatus;
        }
        //	1.1)更新session存活时间
        SecurityUtil.updateSessionTime(cookieId);


        resultStatus.setSuccess(true);
        return resultStatus;
    }




    @Override
    public ResultStatus socketFilter(ChannelHandlerContext ctx, int code) {
        ResultStatus resultStatus = new ResultStatus();
        String sessionId = (String) ctx.channel().attr(AttributeKey.valueOf("token")).get();

        //	1)登录验证
        if(SecurityUtil.isNeedLogin(sessionId , code)){
            resultStatus.setSuccess(false);
            resultStatus.setResponseStatus(HttpResponseStatus.FORBIDDEN);
            return resultStatus;
        }
        //	1.1)更新session存活时间
        SecurityUtil.updateSessionTime(sessionId);


        resultStatus.setSuccess(true);
        return resultStatus;
    }

}
