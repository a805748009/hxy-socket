package nafos.network.bootStrap.netty.handle.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.util.SpringApplicationContextHolder;
import nafos.network.bootStrap.netty.handle.RouteRunnable;


public class HttpRouteRunnable extends RouteRunnable {

	private final HttpRouteClassAndMethod httpRouteClassAndMethod;

	public HttpRouteRunnable(ChannelHandlerContext ctx, FullHttpRequest req, HttpRouteClassAndMethod httpRouteClassAndMethod) {
		super(ctx,req);
		this.httpRouteClassAndMethod = httpRouteClassAndMethod;
	}
	
	

	@Override
	public void run() {
		try{
			SpringApplicationContextHolder.getSpringBeanForClass(HttpRouteHandle.class).route(super.context,super.request,httpRouteClassAndMethod);
		}finally {
			super.taskQueue.complete();
		}

	}
	

    
    
}
