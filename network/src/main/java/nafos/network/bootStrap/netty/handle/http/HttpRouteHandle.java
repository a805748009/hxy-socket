package nafos.network.bootStrap.netty.handle.http;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import nafos.core.Thread.ThreadLocalHelper;
import nafos.core.entry.BusinessException;
import nafos.core.entry.ClassAndMethod;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.entry.http.NafosRequest;
import nafos.core.entry.http.NafosRespone;
import nafos.core.entry.http.NafosThreadInfo;
import nafos.core.helper.ClassAndMethodHelper;
import nafos.core.helper.RequestHelper;
import nafos.core.mode.InitMothods;
import nafos.core.mode.RouteFactory;
import nafos.core.util.*;
import nafos.network.bootStrap.netty.handle.currency.Crc32MessageHandle;
import nafos.network.bootStrap.netty.handle.currency.ZlibMessageHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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


    public void route(ChannelHandlerContext ctx, FullHttpRequest request, HttpRouteClassAndMethod httpRouteClassAndMethod){

        //线程设置request
        ThreadLocalHelper.setThreadInfo(new NafosThreadInfo(new NafosRequest(request)));

        ClassAndMethod filter;
        //  1.前置filter
        if(request.uri().length()>16&&request.uri().substring(0,16).equals(RouteFactory.REMOTE_CALL_URI)){
            filter = InitMothods.getRemoteCallFilter();
        }else{
            filter = InitMothods.getMessageFilter();
        }
        if(!ClassAndMethodHelper.httpCheckResultStatus(filter,ctx,request)) return;

        // 1.安全验证filter
        filter = InitMothods.getHttpSecurityFilter();
        if(!ClassAndMethodHelper.httpCheckResultStatus(filter,ctx,request)) return;

        // 2.消息入口处理
        Object[] contentObj = null;
        try {
            contentObj = getMessageObjOnContent(httpRouteClassAndMethod,request);
//            if(ObjectUtil.isNull(contentObj)){
//                NettyUtil.sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
//                return;
//            }
        } catch (Exception e) {
            NettyUtil.sendError(ctx, HttpResponseStatus.NO_CONTENT);
            e.printStackTrace();
            return;
        }

        // 3.寻找路由成功,返回结果
        long startTime = 0;
        if(httpRouteClassAndMethod.isPrintLog()){
                startTime=System.currentTimeMillis();
            }
        Object returnObj = routeMethod(httpRouteClassAndMethod,contentObj,ctx,request);
        if(httpRouteClassAndMethod.isPrintLog()){
                long endTime=System.currentTimeMillis();
                logger.info("方法："+httpRouteClassAndMethod.getClazz().getName()+"."+httpRouteClassAndMethod.getMethod().getMethodNames()[httpRouteClassAndMethod.getIndex()]+
                        "       程序耗时："+(endTime-startTime)+"ms");
            }


        // 4.发送处理
        sendMethod(httpRouteClassAndMethod,returnObj,ctx,request);
    }



    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午4:12
     * @Description(获取内容消息体)
     * @param
     * @return java.lang.Object
     */
    private Object[] getMessageObjOnContent(HttpRouteClassAndMethod route, FullHttpRequest request) throws Exception {
        if("JSON".equals(route.getType())){
            return RequestHelper.getRequestParams(request,route,null); //json传输方式 不支持任何处理，基本难用到
        }else{
            if(route.getParameters().length==0)//不需要任何参数
                return null;
            byte[] content = RequestHelper.getRequestParamsObj(request);
            if(ObjectUtil.isNull(content))
                throw new Exception("客户端发过来的数据为空");
            content = zlibMessageHandle.unZlibByteMessage(content);//解压
            content = crc32MessageHandle.checkCrc32IntBefore(content);//CRC32校验
            if(ObjectUtil.isNull(content))
                throw new Exception("客户端发过来的数据处理后为空");
            return RequestHelper.getRequestParams(request,route,content);
        }
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午4:36
     * @Description(路由)
     * @param
     * @return java.lang.Object
     */
    private Object routeMethod (HttpRouteClassAndMethod route,Object[] object,ChannelHandlerContext ctx, FullHttpRequest request){
        try {
            if(ObjectUtil.isNull(object)){
                return route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
                            new Object[]{});
            }
            return route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
                    object);
        }catch (BusinessException e){
            return new HttpResponseStatus(e.getCode(),e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return HttpResponseStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午4:38
     * @Description(发送后置处理器)
     * @param
     * @return void
     */
    private void sendMethod (HttpRouteClassAndMethod route,Object object,ChannelHandlerContext context, FullHttpRequest request){
        try {
            //error处理
            if(object instanceof  HttpResponseStatus){
                NettyUtil.sendError(context, (HttpResponseStatus) object);
                return;
            }

            if("JSON".equals(route.getType())){
                send(context, JsonUtil.toJson(object),request,HttpResponseStatus.OK);
            }else{

                //如果回传为null，则直接返回
                if(object==null){
                    send(context,null,request,HttpResponseStatus.OK);
                    return;
                }
                //如果传回的不是byte，那么必定是bean。
                if(!(object instanceof  byte[])){
                    object = ProtoUtil.serializeToByte(object);
                }
                byte[] bytes = crc32MessageHandle.addCrc32IntBefore((byte[])object);
                bytes = zlibMessageHandle.zlibByteMessage(bytes);
                send(context,bytes,request,HttpResponseStatus.OK);
            }

        } catch (IllegalArgumentException | IOException e) {
            NettyUtil.sendError(context, HttpResponseStatus.SERVICE_UNAVAILABLE);
            logger.error(e.toString());
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送的返回值
     * @param <T> context
     * @param ctx     返回
     * @param context 消息
     * @param status 状态
     * @throws UnsupportedEncodingException
     */
    private <T> void send(ChannelHandlerContext ctx, T context,FullHttpRequest request, HttpResponseStatus status) throws UnsupportedEncodingException {
        request.release();
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        //设置允许跨域
        response.headers().set("Access-Control-Allow-Origin", "*");
        NafosRespone sp =  ThreadLocalHelper.getRespone();
        //设置cookie头
        if(ObjectUtil.isNotNull(sp)&&sp.getCookies().size()!=0){
            response.headers().set(HttpHeaderNames.COOKIE, sp.getCookies());
        }
        //
        if(context instanceof byte[]){
            response.content().writeBytes((byte[])context);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/x-protobuf");
        }else if(context instanceof String){
            response.content().writeBytes(((String) context).getBytes());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        }else {
            //为null的时候
            response.content().writeBytes("".getBytes());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        }
        ThreadLocalHelper.threadLocalRemove();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
