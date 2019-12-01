package nafos.server.interceptors;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.server.ClassAndMethod;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:20
 * @Description 获取http消息之前监听处理类
 **/
public class InterceptorsFactory {

    private static Map<Class, ClassAndMethod> interceptors = new HashMap<>();

    public InterceptorsFactory(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(InterceptorInterface.class);
        for (String filterName : filterNames) {
            MethodAccess filterMa = MethodAccess.get(context.getType(filterName));
            int index = 0;
            interceptors.put(context.getType(filterName), new ClassAndMethod(context.getType(filterName), filterMa, index));
        }
    }

    public static ClassAndMethod getInterceptor(Class clazz) {
        return interceptors.get(clazz);
    }

}
