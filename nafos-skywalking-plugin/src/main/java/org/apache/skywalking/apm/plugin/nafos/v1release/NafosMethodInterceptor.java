package org.apache.skywalking.apm.plugin.nafos.v1release;

import io.netty.channel.ChannelHandlerContext;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.core.entry.HttpRouteClassAndMethod;
import org.apache.skywalking.apm.agent.core.context.CarrierItem;
import org.apache.skywalking.apm.agent.core.context.ContextCarrier;
import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.tag.Tags;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.agent.core.context.trace.SpanLayer;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

import java.lang.reflect.Method;

/**
 * {@link NafosMethodInterceptor} intercept method of {@link nafos.bootStrap.handle.http.HttpRouteHandle#route(ChannelHandlerContext ctx, NsRequest request, HttpRouteClassAndMethod httpRouteClassAndMethod)}
 * operation.
 *
 * @author hxy
 */
public class NafosMethodInterceptor implements InstanceMethodsAroundInterceptor {

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                             Class<?>[] argumentsTypes, MethodInterceptResult result)  {
        NsRequest request = (NsRequest) allArguments[1];
        ContextCarrier contextCarrier = new ContextCarrier();
        CarrierItem next = contextCarrier.items();
        while (next.hasNext()) {
            next = next.next();
            next.setHeadValue(request.headers().get(next.getHeadKey()));
        }
        AbstractSpan span = ContextManager.createEntrySpan(parseUri(request.getUri()), contextCarrier);
        Tags.URL.set(span, request.getUri());
        Tags.HTTP.METHOD.set(span, request.method().name());
        span.setComponent(ComponentsDefine.SPRING_REST_TEMPLATE);
        SpanLayer.asHttp(span);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                              Object ret)  {
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                      Class<?>[] argumentsTypes, Throwable t) {
        ContextManager.activeSpan().errorOccurred().log(t);
    }

    private String parseUri(String uri) {
        if (null == uri) {
            return null;
        }
        int index = uri.indexOf("?");
        if (-1 != index) {
            uri = uri.substring(0, index);
        }
        return uri;
    }


}
