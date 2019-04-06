package nafos.core.task;

import nafos.bootStrap.handle.http.HttpRouteHandle;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.bootStrap.handle.socket.AbstractSocketRouteHandle;
import nafos.bootStrap.handle.socket.IocBeanFactory;
import nafos.core.entry.AsyncTaskMode;
import nafos.core.task.TaskQueue;
import nafos.core.util.ObjectUtil;
import nafos.core.helper.SpringApplicationContextHolder;


import java.util.concurrent.ExecutorService;

/**
 * 任务处理队列.
 */
public class RouteTaskQueue extends TaskQueue {

    public RouteTaskQueue(ExecutorService threadPool) {
        super(threadPool);
    }

    @Override
    public void complete(Object object) {
        try {
            if (object instanceof AsyncTaskMode) {
                AsyncTaskMode mode = (AsyncTaskMode) object;
                if (ObjectUtil.isNotNull(mode.getHttpRouteClassAndMethod())) {
                    SpringApplicationContextHolder.getSpringBeanForClass(HttpRouteHandle.class)
                            .route(mode.getCtx(), (NsRequest) mode.getRequest(), mode.getHttpRouteClassAndMethod());
                } else {
                    SpringApplicationContextHolder.getContext().getBean(IocBeanFactory.getSocketRouthandle(), AbstractSocketRouteHandle.class)
                            .route(mode.getCtx(), mode.getSocketRouteClassAndMethod(), mode.getBody(), mode.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}