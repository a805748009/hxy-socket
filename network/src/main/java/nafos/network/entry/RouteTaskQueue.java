package nafos.network.entry;

import nafos.core.entry.AsyncTaskMode;
import nafos.core.task.TaskQueue;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SpringApplicationContextHolder;
import nafos.network.bootStrap.netty.handle.RouteRunnable;
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

	/**
	 * 往任务队列里提交一个任务。
	 * 
	 * @param task 任务
	 */
	public void submit(RouteRunnable task) {
		task.setTaskQueue(this);
		synchronized (this) {
			queue.add(task);
			// 只有一个任务，那就是刚刚加的，直接开始执行...
			if (queue.size() == 1) {
				threadPool.execute(task);
			}
		}
	}

	public void submitHttpOnWork(AsyncTaskMode mode) {
		synchronized (this) {
			queue.add(mode);
			// 只有一个任务，那就是刚刚加的，直接开始执行...
			if (queue.size() == 1) {
				try{
					SpringApplicationContextHolder.getSpringBeanForClass(HttpRouteHandle.class)
							.route(mode.getCtx(), mode.getRequest(),mode.getHttpRouteClassAndMethod());
				}finally {
					complete();
				}

			}
		}
	}

	public void submitSocketOnWork(AsyncTaskMode mode) {
		synchronized (this) {
			queue.add(mode);
			// 只有一个任务，那就是刚刚加的，直接开始执行...
			if (queue.size() == 1) {
				try {
					SpringApplicationContextHolder.getContext().getBean(IocBeanFactory.getSocketRouthandle(),AbstractSocketRouteHandle.class)
							.route(mode.getCtx(), mode.getSocketRouteClassAndMethod(), mode.getBody(), mode.getId());
				}finally {
					complete();
				}
			}
		}
	}

	/**
	 * 完成一个任务后续处理
	 */
	public void complete() {
		synchronized (this) {
			// 移除已经完成的任务。
			queue.removeFirst();

			// 完成一个任务后，如果还有任务，则继续执行。
			if (!queue.isEmpty()) {

				Object first = queue.getFirst();

				if(first instanceof RouteRunnable)
					this.threadPool.submit((RouteRunnable) first);

				if(first instanceof AsyncTaskMode){
					AsyncTaskMode mode = (AsyncTaskMode) first;
					if(ObjectUtil.isNotNull(mode.getHttpRouteClassAndMethod())){
						SpringApplicationContextHolder.getSpringBeanForClass(HttpRouteHandle.class)
								.route(mode.getCtx(), mode.getRequest(),mode.getHttpRouteClassAndMethod());
					}else{
						SpringApplicationContextHolder.getSpringBeanForClass(AbstractSocketRouteHandle.class)
								.route(mode.getCtx(), mode.getSocketRouteClassAndMethod(),mode.getBody(),mode.getId());
					}
				}



			}
		}
	}
}