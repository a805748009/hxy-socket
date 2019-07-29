package nafos.bootStrap.handle.http;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import nafos.bootStrap.handle.currency.Crc32MessageHandle;
import nafos.bootStrap.handle.currency.ZlibMessageHandle;
import nafos.core.Enums.Protocol;
import nafos.core.Thread.ThreadLocalHelper;
import nafos.core.entry.ClassAndMethod;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.entry.error.BizException;
import nafos.core.entry.http.ReqResBean;
import nafos.core.helper.ClassAndMethodHelper;
import nafos.core.helper.RequestHelper;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.InitMothods;
import nafos.core.monitor.RunWatch;
import nafos.core.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午12:45
 * @Description http 选择路由
 **/
@Service
public class HttpRouteHandle {
    private static final Logger logger = LoggerFactory.getLogger(HttpRouteHandle.class);
    @Autowired
    Crc32MessageHandle crc32MessageHandle;
    @Autowired
    ZlibMessageHandle zlibMessageHandle;


    public void route(ChannelHandlerContext ctx, NsRequest request, HttpRouteClassAndMethod httpRouteClassAndMethod) {

        //线程设置request
        ThreadLocalHelper.setThreadInfo(new ReqResBean(request));

        ClassAndMethod filter;


        //  1.前置filter 拦截器
        for (Class interceptor : httpRouteClassAndMethod.getInterceptors()) {
            filter = InitMothods.getInterceptor(interceptor);
            if (filter == null) {
                logger.warn("{} :拦截器没有实现InterceptorInterface,或者继承AbstractHttpInterceptor. 拦截无效", interceptor);
                continue;
            }
            filter.setIndex(0);
            if (!ClassAndMethodHelper.checkResultStatus(filter, ctx, request)) return;
        }

        // 2.消息入口处理
        Object[] contentObj = null;
        try {
            contentObj = getMessageObjOnContent(httpRouteClassAndMethod, request);
        } catch (Exception e) {
            NettyUtil.sendError(ctx, HttpResponseStatus.NO_CONTENT);
            e.printStackTrace();
            return;
        }

        // 3.寻找路由成功,返回结果
        String methodName = httpRouteClassAndMethod.getClazz().getName() + "." + httpRouteClassAndMethod.getMethod().getMethodNames()[httpRouteClassAndMethod.getIndex()];
        RunWatch runWatch = RunWatch.init(methodName);

        Object returnObj = routeMethod(httpRouteClassAndMethod, contentObj);

        if (httpRouteClassAndMethod.isPrintLog()) {
            logger.info("方法：" + methodName + "       程序耗时：" + runWatch.stop() + "ms");
        } else {
            runWatch.stop();
        }

        // 4.发送处理
        sendMethod(httpRouteClassAndMethod, returnObj, ctx, request);
    }


    /**
     * @param
     * @return java.lang.Object
     * @Author 黄新宇
     * @date 2018/7/4 下午4:12
     * @Description(获取内容消息体)
     */
    private Object[] getMessageObjOnContent(HttpRouteClassAndMethod route, NsRequest request) {
        if (route.getType() == Protocol.JSON) {
            return RequestHelper.getRequestParams(request, route);
        } else {
            if (route.getParameters().length == 0)//不需要任何参数
                return null;

            if (request.method() == HttpMethod.GET) {
                return RequestHelper.getRequestParams(request, route);
            }

            byte[] content = RequestHelper.getRequestParamsObj(request);
            if (ObjectUtil.isNull(content))
                throw new NullPointerException(request.uri() + ":  客户端发过来的数据为空");
            content = zlibMessageHandle.unZlibByteMessage(content);//解压
            content = crc32MessageHandle.checkCrc32IntBefore(content);//CRC32校验
            if (ObjectUtil.isNull(content))
                throw new NullPointerException(request.uri() + ":   客户端发过来的数据处理后为空");
            return RequestHelper.getRequestParams(request, route, content);
        }
    }

    /**
     * @param
     * @return java.lang.Object
     * @Author 黄新宇
     * @date 2018/7/4 下午4:36
     * @Description(路由)
     */
    private Object routeMethod(HttpRouteClassAndMethod route, Object[] object) {
        try {
            if (ObjectUtil.isNull(object)) {
                return route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()), route.getIndex(),
                        new Object[]{});
            }
            return route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()), route.getIndex(),
                    object);
        } catch (BizException e) {
            return e;
        } catch (Exception e) {
            logger.error("程序异常：{}",e.toString());
            e.printStackTrace();
            return HttpResponseStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * @param
     * @return void
     * @Author 黄新宇
     * @date 2018/7/4 下午4:38
     * @Description(发送后置处理器)
     */
    private void sendMethod(HttpRouteClassAndMethod route, Object object, ChannelHandlerContext context, FullHttpRequest request) {
        //error处理
        if (object instanceof BizException) {
            request.release();
            NettyUtil.sendError(context, (BizException) object);
            return;
        }
        if (object instanceof HttpResponseStatus) {
            request.release();
            NettyUtil.sendError(context, (HttpResponseStatus) object);
            return;
        }

        if (route.getType() == Protocol.JSON) {
            send(context, JsonUtil.toJsonIsNotNull(object), request);
        } else {

            //如果回传为null，则直接返回
            if (object == null) {
                send(context, null, request);
                return;
            }
            //如果传回的不是byte，那么必定是bean。
            if (!(object instanceof byte[])) {
                object = ProtoUtil.serializeToByte(object);
            }
            byte[] bytes = crc32MessageHandle.addCrc32IntBefore((byte[]) object);
            bytes = zlibMessageHandle.zlibByteMessage(bytes);
            send(context, bytes, request);
        }
    }


    /**
     * 发送的返回值
     *
     * @param <T>     context
     * @param ctx     返回
     * @param context 消息
     * @throws UnsupportedEncodingException
     */
    private <T> void send(ChannelHandlerContext ctx, T context, FullHttpRequest request) {
        request.release();
        NsRespone response = ThreadLocalHelper.getRespone();
        //设置允许跨域
        response.headers().set("Access-Control-Allow-Origin", "*");
        //设置cookie头
        if (response.getCookies().size() != 0) {
            response.headers().set(HttpHeaderNames.COOKIE, response.getCookies());
        }
        //
        if (context instanceof byte[]) {
            response.content().writeBytes((byte[]) context);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/x-protobuf");
        } else if (context instanceof String) {
            response.content().writeBytes(((String) context).getBytes());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        } else {
            //为null的时候
            response.content().writeBytes("".getBytes());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        }
        ThreadLocalHelper.threadLocalRemove();

        if (ctx.channel().isActive())
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
