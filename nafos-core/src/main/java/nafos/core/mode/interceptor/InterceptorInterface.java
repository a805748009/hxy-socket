package nafos.core.mode.interceptor;

import io.netty.channel.ChannelHandlerContext;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.core.entry.ResultStatus;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月9日 下午3:45:14 
* http请求过来的前置处理器
*/
public interface InterceptorInterface {

	ResultStatus interptor(ChannelHandlerContext ctx, NsRequest req,String param);

	ResultStatus interptor(ChannelHandlerContext ctx, int code,String param);

}
