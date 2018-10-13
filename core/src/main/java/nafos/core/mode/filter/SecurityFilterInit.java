package nafos.core.mode.filter;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.entry.ClassAndMethod;
import nafos.core.mode.filter.fInterface.SecurityFilter;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:20
 * @Description 获取安全验证监听处理类
 **/
public class SecurityFilterInit {

    private ClassAndMethod httpClassAndMethod;

    private ClassAndMethod socketClassAndMethod;

    public SecurityFilterInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(SecurityFilter.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("httpFilter");
            httpClassAndMethod =  new ClassAndMethod(context.getType(filterNames[0]), filterMa, index);

            int index2 = filterMa.getIndex("socketFilter");
            socketClassAndMethod =  new ClassAndMethod(context.getType(filterNames[0]), filterMa, index2);
        }
    }

    public ClassAndMethod getHttpSecurityFilter() {
        return httpClassAndMethod;
    }

    public ClassAndMethod getSocketSecurityFilter() {
        return socketClassAndMethod;
    }

}
