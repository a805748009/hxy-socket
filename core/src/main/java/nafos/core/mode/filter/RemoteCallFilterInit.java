package nafos.core.mode.filter;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.entry.ClassAndMethod;
import nafos.core.mode.filter.fInterface.RemoteCallFilter;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/6/11 下午6:05
 * @Description TODO
 **/
public class RemoteCallFilterInit {
    private ClassAndMethod classAndMcethod;

    public RemoteCallFilterInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(RemoteCallFilter.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("filter");
            classAndMcethod =  new ClassAndMethod(context.getType(filterNames[0]), filterMa, index);
        }
    }

    public ClassAndMethod getFilter() {
        return classAndMcethod;
    }
}
