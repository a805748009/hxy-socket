package nafos.core.mode.filter;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.entry.ClassAndMethod;
import nafos.core.mode.filter.fInterface.HttpMessageFilter;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:20
 * @Description 获取http消息之前监听处理类
 **/
public class HttpMessageFilterInit {

    private ClassAndMethod classAndMethod;

    public HttpMessageFilterInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(HttpMessageFilter.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("filter");
            classAndMethod =  new ClassAndMethod(context.getType(filterNames[0]), filterMa, index);
        }
    }

    public ClassAndMethod getFilter() {
        return classAndMethod;
    }

}
