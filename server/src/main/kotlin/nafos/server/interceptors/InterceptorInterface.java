package nafos.server.interceptors;

import io.netty.channel.ChannelHandlerContext;
import nafos.server.handle.http.NsRequest;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月9日 下午3:45:14 
* http请求过来的前置处理器
*/
public interface InterceptorInterface {

	ResultStatus interptor(ChannelHandlerContext ctx, Object reqOrCode, String param);

}
