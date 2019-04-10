package nafos.bootStrap.handle.socket;

import io.netty.channel.ChannelHandlerContext;
import nafos.bootStrap.handle.ExecutorPoolChoose;
import nafos.bootStrap.handle.currency.ExcuteHandle;
import nafos.bootStrap.handle.http.HttpRouteHandle;
import nafos.core.Thread.ExecutorPool;
import nafos.core.entry.ClassAndMethod;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.helper.ClassAndMethodHelper;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.InitMothods;
import nafos.core.util.ArrayUtil;
import nafos.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @Author 黄新宇
 * @Date 2018/10/8 下午9:49
 * @Description 分解消息体并根据controller选择是在work线程执行还是业务线程执行
 **/
@Service
public class SocketExecutorPoolChoose implements ExecutorPoolChoose {
    private static final Logger logger = LoggerFactory.getLogger(SocketExecutorPoolChoose.class);


    @Override
    public void choosePool(ChannelHandlerContext ctx, Object msg) {
        byte[] contentBytes = (byte[]) msg;

        byte[] idByte = new byte[4];//前端传过来的ID，原样返回
        System.arraycopy(contentBytes, 0, idByte, 0, 4);

        byte[] uriByte = new byte[4];//解析路由的uri
        System.arraycopy(contentBytes, 4, uriByte, 0, 4);
        int code = ArrayUtil.byteArrayToInt(uriByte);

        byte[] messageBody = new byte[contentBytes.length - 8];
        if (contentBytes.length > 8) {
            System.arraycopy(contentBytes, 8, messageBody, 0, contentBytes.length - 8);
        }

        logger.debug("收到socket消息，id: " + code);

        SocketRouteClassAndMethod socketRouteClassAndMethod = InitMothods.getSocketHandler(code);
        if (ObjectUtil.isNull(socketRouteClassAndMethod)) {
            logger.error("{} :找不到匹配的路由", code);
            return;
        }

        //切面拦截器
        ClassAndMethod filter;
        for (Class interceptor : socketRouteClassAndMethod.getInterceptors()) {
            filter = InitMothods.getInterceptor(interceptor);
            if (filter == null) {
                logger.warn("{} :拦截器没有实现InterceptorInterface,或者继承AbstractSocketInterceptor. 拦截无效", interceptor);
                continue;
            }
            filter.setIndex(1);
            if (!ClassAndMethodHelper.checkResultStatus(filter, ctx, code)) return;
        }

        boolean isRunOnWork = socketRouteClassAndMethod.isRunOnWorkGroup();

        if (!isRunOnWork) {
            ExecutorPool.getInstance().execute(new ExcuteHandle(ctx, socketRouteClassAndMethod, messageBody, idByte));
            return;
        }

        SpringApplicationContextHolder.getContext().getBean(IocBeanFactory.getSocketRouthandle(), AbstractSocketRouteHandle.class)
                .route(ctx, socketRouteClassAndMethod, messageBody, idByte);


    }
}
