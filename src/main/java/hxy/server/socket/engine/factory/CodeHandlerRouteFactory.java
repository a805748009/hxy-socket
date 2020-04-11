package hxy.server.socket.engine.factory;

import com.esotericsoftware.reflectasm.MethodAccess;
import hxy.server.socket.anno.CodeHandler;
import hxy.server.socket.anno.Handle;
import hxy.server.socket.engine.ChannelActive;
import hxy.server.socket.entity.CodeHandlerBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CodeHandlerRouteFactory {

    private static final Logger logger = LoggerFactory.getLogger(CodeHandlerRouteFactory.class);

    private final static Map<Integer, CodeHandlerBean> CODE_HANDLER_MAP = new HashMap<>();

    private static ChannelActive[] channelActives;

    /***
     *@Description 预加载所有codeHandler
     *@Author xinyu.huang
     *@Time 2020/4/11 14:38
     */
    public static void load(ApplicationContext context) {
        loadActiveHandler(context);
        loadMessageHandler(context);
    }

    public static CodeHandlerBean getCodeHandlerBean(int code) {
        return CODE_HANDLER_MAP.get(code);
    }

    public static ChannelActive[] getChannelActives() {
        return channelActives;
    }

    private static void loadActiveHandler(ApplicationContext context) {
        Map<String, ChannelActive> channelActiveMap = context.getBeansOfType(ChannelActive.class);
        ChannelActive[] channelActiveArray = new ChannelActive[channelActiveMap.size()];
        AtomicInteger i = new AtomicInteger();
        channelActiveMap.values().forEach(channelActive -> {
            channelActiveArray[i.get()] = channelActive;
            i.getAndIncrement();
        });
        channelActives = channelActiveArray;
    }

    private static void loadMessageHandler(ApplicationContext context) {
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(CodeHandler.class);
        taskBeanMap.forEach((key, value) -> {
            Class clz = context.getType(key);
            Set<Method> methods = MethodIntrospector.selectMethods(clz, (ReflectionUtils.MethodFilter) method ->
                    AnnotatedElementUtils.findMergedAnnotation(method, Handle.class) != null);
            for (Method method : methods) {
                Handle handle = AnnotatedElementUtils.findMergedAnnotation(method, Handle.class);
                MethodAccess ma = MethodAccess.get(clz);
                assert handle != null;
                Integer code = handle.code();
                checkExistsCodeHandler(clz, code);
                CODE_HANDLER_MAP.put(code, new CodeHandlerBean(clz, ma,
                        ma.getIndex(method.getName()), method.getParameterTypes(), value));
                logger.debug("load codeHandler -> class:{}  method:{}", clz, method.getName());
            }
        });
    }

    private static void checkExistsCodeHandler(Class<?> clz, Integer code) {
        CodeHandlerBean existsCode = getCodeHandlerBean(code);
        if (existsCode != null) {
            throw new UnsupportedOperationException(String.format("duplicate codeHandler , code :%s,class:[%s,%s]", code, clz, existsCode.getClazz()));
        }
    }

}
