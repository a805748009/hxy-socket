package nafos.bootStrap.handle.currency;

import io.netty.channel.ChannelHandlerContext;
import nafos.bootStrap.handle.http.HttpRouteHandle;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.bootStrap.handle.socket.AbstractSocketRouteHandle;
import nafos.bootStrap.handle.socket.IocBeanFactory;
import nafos.bootStrap.handle.socket.SocketRouteHandle;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.util.ObjectUtil;

/**
 * 线程执行外部handle方法
 */
public class ExcuteHandle implements Runnable {

    private static HttpRouteHandle httpRouteHandle = SpringApplicationContextHolder.getSpringBeanForClass(HttpRouteHandle.class);

    private static AbstractSocketRouteHandle abstractSocketRouteHandle =  SpringApplicationContextHolder.getSpringBeanForClass(SocketRouteHandle.class);

    private ChannelHandlerContext ctx;

    private NsRequest request;

    private HttpRouteClassAndMethod httpRouteClassAndMethod;

    private SocketRouteClassAndMethod socketRouteClassAndMethod;

    private byte[] messageBody;

    private byte[] idByte;

    public ExcuteHandle() {

    }

    public ExcuteHandle(ChannelHandlerContext ctx, NsRequest request, HttpRouteClassAndMethod httpRouteClassAndMethod) {
        this.ctx = ctx;
        this.request = request;
        this.httpRouteClassAndMethod = httpRouteClassAndMethod;
    }

    public ExcuteHandle(ChannelHandlerContext ctx, SocketRouteClassAndMethod socketRouteClassAndMethod, byte[] messageBody, byte[] idByte) {
        this.ctx = ctx;
        this.socketRouteClassAndMethod = socketRouteClassAndMethod;
        this.messageBody = messageBody;
        this.idByte = idByte;
    }

    public static void setHttpRouteHandle(HttpRouteHandle hrh){
        httpRouteHandle = hrh;
    }

    public static void setAbstractSocketRouteHandle(AbstractSocketRouteHandle asrh){
        abstractSocketRouteHandle = asrh;
    }

    @Override
    public void run() {
        if (ObjectUtil.isNotNull(httpRouteClassAndMethod)) {
            httpRouteHandle.route(ctx, request, httpRouteClassAndMethod);
        } else {
            abstractSocketRouteHandle.route(ctx, socketRouteClassAndMethod, messageBody, idByte);
        }
    }
}
