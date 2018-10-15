package nafos.network.bootStrap.netty.handle.socket;

import io.netty.channel.ChannelHandlerContext;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.util.SpringApplicationContextHolder;
import nafos.network.bootStrap.netty.handle.RouteRunnable;


public class SocketRouteRunnable extends RouteRunnable {

	private final SocketRouteClassAndMethod socketRouteClassAndMethod;

	private  byte[] idByte;

	public SocketRouteRunnable(ChannelHandlerContext ctx,  SocketRouteClassAndMethod socketRouteClassAndMethod,byte[] messageBody,byte[] idByte) {
		super(ctx,messageBody);
		this.socketRouteClassAndMethod = socketRouteClassAndMethod;
		this.idByte = idByte;
	}
	
	

	@Override
	public void run() {
		try{
			SpringApplicationContextHolder.getContext().getBean(IocBeanFactory.getSocketRouthandle(),AbstractSocketRouteHandle.class)
					.route(super.context,socketRouteClassAndMethod,super.body,idByte);
		}finally {
			super.taskQueue.complete();
		}
	}
	

    
    
}
