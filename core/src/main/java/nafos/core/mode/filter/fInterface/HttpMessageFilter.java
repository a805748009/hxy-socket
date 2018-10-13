package nafos.core.mode.filter.fInterface;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import nafos.core.entry.ResultStatus;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月9日 下午3:45:14 
* http请求过来的前置处理器
*/
public interface HttpMessageFilter {
	
	ResultStatus filter(ChannelHandlerContext ctx, FullHttpRequest req);

}
