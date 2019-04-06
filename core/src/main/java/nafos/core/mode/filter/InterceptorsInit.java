package nafos.core.mode.filter;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.entry.ClassAndMethod;
import nafos.core.mode.filter.fInterface.InterceptorInterface;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:20
 * @Description 获取http消息之前监听处理类
 **/
public class InterceptorsInit {

    private Map<Class, ClassAndMethod> interceptors = new HashMap<>();

    public InterceptorsInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(InterceptorInterface.class);
        for (String filterName : filterNames) {
            MethodAccess filterMa = MethodAccess.get(context.getType(filterName));
            int index = 0;
            interceptors.put(context.getType(filterName), new ClassAndMethod(context.getType(filterName), filterMa, index));
        }
    }

    public Map<Class, ClassAndMethod> getInterceptors() {
        return interceptors;
    }

}
