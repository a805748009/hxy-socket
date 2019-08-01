package org.apache.skywalking.apm.plugin.mongodb.v310;

import com.mongodb.ServerAddress;
import com.mongodb.connection.SplittablePayload;
import com.mongodb.internal.connection.InternalConnection;
import org.apache.skywalking.apm.agent.core.conf.Config;
import org.apache.skywalking.apm.agent.core.context.ContextCarrier;
import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.tag.Tags;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.agent.core.context.trace.SpanLayer;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceConstructorInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;
import org.bson.BsonDocument;

import java.lang.reflect.Method;

public class MongoDBMethodInterceptor implements InstanceMethodsAroundInterceptor, InstanceConstructorInterceptor {

    private static final String DB_TYPE = "MongoDB";

    private static final String MONGO_DB_OP_PREFIX = "MongoDB/";

    private static final int FILTER_LENGTH_LIMIT = 256;


    private String limitFilter(String filter) {
        final StringBuilder params = new StringBuilder();
        if (filter.length() > FILTER_LENGTH_LIMIT) {
            return params.append(filter, 0, FILTER_LENGTH_LIMIT).append("...").toString();
        } else {
            return filter;
        }
    }

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                             Class<?>[] argumentsTypes, MethodInterceptResult result) {
        String remoteAddress = (String) objInst.getSkyWalkingDynamicField();
        BsonDocument bson = (BsonDocument) allArguments[1];
        String type = bson.getFirstKey();
        String body = bson.entrySet().toArray()[1].toString();
        SplittablePayload payload = (SplittablePayload) allArguments[7];
        // insert 或者updata等方法
        if (payload != null) {
            type = payload.getPayloadType().toString();
            body = payload.getPayload().toString();
        }
        String executeMethod = "[" + allArguments[0] + " - " + bson.getString(bson.getFirstKey()) + "]:" + type;
        AbstractSpan span = ContextManager.createExitSpan(MONGO_DB_OP_PREFIX + executeMethod, new ContextCarrier(), remoteAddress);
        span.setComponent(ComponentsDefine.MONGO_DRIVER);
        Tags.DB_TYPE.set(span, DB_TYPE);
        SpanLayer.asDB(span);

        Config.Plugin.MongoDB.TRACE_PARAM = true;
        if (Config.Plugin.MongoDB.TRACE_PARAM) {
            Tags.DB_STATEMENT.set(span, executeMethod + "  -  " + limitFilter(body));
        }

    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                              Class<?>[] argumentsTypes, Object ret) {
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                      Class<?>[] argumentsTypes, Throwable t) {
        AbstractSpan activeSpan = ContextManager.activeSpan();
        activeSpan.errorOccurred();
        activeSpan.log(t);
    }


    @Override
    public void onConstruct(EnhancedInstance enhancedInstance, Object[] allArguments) {
        InternalConnection ici = (InternalConnection) allArguments[0];
        ServerAddress serverAddress = ici.getDescription().getServerAddress();
        String remoteAddress = serverAddress.getHost() + ":" + serverAddress.getPort();
        enhancedInstance.setSkyWalkingDynamicField(remoteAddress);
    }
}
