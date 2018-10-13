package nafos.network.bootStrap.netty.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import nafos.core.task.TaskQueue;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午4:37
 * @Description TODO
 **/
public class RouteRunnable implements Runnable{

    // 保存变量
    protected ChannelHandlerContext context;
    protected FullHttpRequest request;
    protected byte[] body;
    protected TaskQueue taskQueue;



    public RouteRunnable(ChannelHandlerContext ctx, FullHttpRequest req) {
        ReferenceCountUtil.retain(req);
        context = ctx;
        request = req;
        this.taskQueue = taskQueue;
    }

    public RouteRunnable(ChannelHandlerContext ctx, byte[] body) {
        context = ctx;
        this.body = body;
        this.taskQueue = taskQueue;
    }

    public void setTaskQueue(TaskQueue taskQueue){
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {

    }
}
