package nafos.network.entry;

import nafos.core.entry.AsyncTaskMode;
import nafos.core.task.TaskQueue;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SpringApplicationContextHolder;
import nafos.network.bootStrap.netty.handle.http.HttpRouteHandle;
import nafos.network.bootStrap.netty.handle.socket.AbstractSocketRouteHandle;
import nafos.network.bootStrap.netty.handle.socket.IocBeanFactory;

import java.util.concurrent.ExecutorService;

/**
 * 任务处理队列.
 */
public class RouteTaskQueue extends TaskQueue{

	public RouteTaskQueue(ExecutorService threadPool) {
		super(threadPool);
	}

	@Override
	public void complete(Object object) {
		try{
			if(object instanceof AsyncTaskMode){
				AsyncTaskMode mode = (AsyncTaskMode) object;
				if(ObjectUtil.isNotNull(mode.getHttpRouteClassAndMethod())){
					SpringApplicationContextHolder.getSpringBeanForClass(HttpRouteHandle.class)
							.route(mode.getCtx(), mode.getRequest(),mode.getHttpRouteClassAndMethod());
				}else{
					SpringApplicationContextHolder.getContext().getBean(IocBeanFactory.getSocketRouthandle(),AbstractSocketRouteHandle.class)
							.route(mode.getCtx(), mode.getSocketRouteClassAndMethod(), mode.getBody(), mode.getId());
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}