package nafos.core.entry;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @Author 黄新宇
 * @Date 2018/10/11 下午3:59
 * @Description TODO
 **/
public class AsyncTaskMode {

    private ChannelHandlerContext ctx;

    private FullHttpRequest request;

    private HttpRouteClassAndMethod httpRouteClassAndMethod;

    private SocketRouteClassAndMethod socketRouteClassAndMethod;

    private byte[] body;

    private byte[] id;

    public AsyncTaskMode(ChannelHandlerContext ctx, FullHttpRequest request, HttpRouteClassAndMethod httpRouteClassAndMethod) {
        this.ctx = ctx;
        this.request = request;
        this.httpRouteClassAndMethod = httpRouteClassAndMethod;
    }

    public AsyncTaskMode(ChannelHandlerContext ctx, SocketRouteClassAndMethod socketRouteClassAndMethod, byte[] body, byte[] id) {
        this.ctx = ctx;
        this.socketRouteClassAndMethod = socketRouteClassAndMethod;
        this.body = body;
        this.id = id;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public HttpRouteClassAndMethod getHttpRouteClassAndMethod() {
        return httpRouteClassAndMethod;
    }

    public void setHttpRouteClassAndMethod(HttpRouteClassAndMethod httpRouteClassAndMethod) {
        this.httpRouteClassAndMethod = httpRouteClassAndMethod;
    }

    public SocketRouteClassAndMethod getSocketRouteClassAndMethod() {
        return socketRouteClassAndMethod;
    }

    public void setSocketRouteClassAndMethod(SocketRouteClassAndMethod socketRouteClassAndMethod) {
        this.socketRouteClassAndMethod = socketRouteClassAndMethod;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }
}
