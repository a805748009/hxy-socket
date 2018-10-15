package nafos.network.bootStrap.netty.handle.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import nafos.core.entry.AsyncTaskMode;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.mode.InitMothods;
import nafos.core.util.ArrayUtil;
import nafos.core.util.ObjectUtil;
import nafos.network.bootStrap.netty.handle.ExecutorPoolChoose;
import nafos.network.bootStrap.netty.handle.currency.AsyncSessionHandle;
import nafos.network.entry.RouteTaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author 黄新宇
 * @Date 2018/10/8 下午9:49
 * @Description 分解消息体并根据controller选择是在work线程执行还是业务线程执行
 **/
@Service
public class SocketExecutorPoolChoose implements ExecutorPoolChoose {
    private static final Logger logger = LoggerFactory.getLogger(SocketExecutorPoolChoose.class);

    @Autowired
    SocketRouteHandle socketRouteHandle;



    @Override
    public void choosePool(ChannelHandlerContext ctx, Object msg) {
        byte[] contentBytes = (byte[]) msg;

        byte[] idByte = new byte[4];//前端传过来的ID，原样返回
        System.arraycopy(contentBytes, 0, idByte, 0, 4);

        byte[] uriByte = new byte[4];//解析路由的uri
        System.arraycopy(contentBytes, 4, uriByte, 0, 4);
        int code = ArrayUtil.byteArrayToInt(uriByte);

        byte[] messageBody = new byte[contentBytes.length-8];
        if(contentBytes.length>8){
            System.arraycopy(contentBytes, 8, messageBody, 0, contentBytes.length-8);
        }

        SocketRouteClassAndMethod socketRouteClassAndMethod = InitMothods.getSocketHandler(code);
        if(ObjectUtil.isNull(socketRouteClassAndMethod)){
            logger.error("找不到路由=");
            return;
        }

        boolean isRunOnWork = socketRouteClassAndMethod.isRunOnWorkGroup();

        String cookieId = ctx.channel().id().toString();
        int queuecCode = cookieId.hashCode()%10;

        if(!isRunOnWork){
            SocketRouteRunnable runnable = new SocketRouteRunnable(ctx, socketRouteClassAndMethod,messageBody,idByte);
            AsyncSessionHandle.runTask(queuecCode,runnable);
            return;
        }

        ReferenceCountUtil.retain(msg);
        ( AsyncSessionHandle.getTask(queuecCode)).submitSocketOnWork(new AsyncTaskMode(ctx, socketRouteClassAndMethod,messageBody,idByte));

    }
}
