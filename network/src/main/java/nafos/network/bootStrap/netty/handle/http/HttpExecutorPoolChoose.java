package nafos.network.bootStrap.netty.handle.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.ReferenceCountUtil;
import nafos.core.Thread.Processors;
import nafos.core.entry.AsyncTaskMode;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.entry.http.NafosRequest;
import nafos.core.mode.InitMothods;
import nafos.core.util.CastUtil;
import nafos.core.util.NettyUtil;
import nafos.core.util.ObjectUtil;
import nafos.core.util.UriUtil;
import nafos.network.bootStrap.netty.handle.ExecutorPoolChoose;
import nafos.network.bootStrap.netty.handle.currency.AsyncSessionHandle;
import nafos.network.entry.RouteTaskQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @Author 黄新宇
 * @Date 2018/10/8 下午9:49
 * @Description TODO
 **/
@Service
public class HttpExecutorPoolChoose implements ExecutorPoolChoose {
    @Autowired
    HttpRouteHandle httpRouteHandle;

    @Override
    public void choosePool(ChannelHandlerContext ctx, Object msg) {
        FullHttpRequest request = (FullHttpRequest) msg;

        // 根据URI来查找object.method.invoke
        String uri = request.method()+":"+ UriUtil.parseUri(request.uri());

        if("GET:/favicon.ico".equals(uri))return;

        HttpRouteClassAndMethod httpRouteClassAndMethod = InitMothods.getHttpHandler(uri);

        // 1.寻找路由失败
        if(httpRouteClassAndMethod==null){
            NettyUtil.sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        boolean isRunOnWork = httpRouteClassAndMethod.isRunOnWorkGroup();

        String cookieId = new NafosRequest(request).getNafosCookieId();
        cookieId = ObjectUtil.isNotNull(cookieId)?cookieId: CastUtil.castString(new Random().nextInt(10));
        int queuecCode = cookieId.hashCode()% Processors.getProcess();

        if(!isRunOnWork){
            HttpRouteRunnable runnable = new HttpRouteRunnable(ctx, request,httpRouteClassAndMethod);
            AsyncSessionHandle.runTask(queuecCode,runnable);
            return;
        }

        ( AsyncSessionHandle.getTask(queuecCode)).submitHttpOnWork(new AsyncTaskMode(ctx,request,httpRouteClassAndMethod));


    }
}
