package nafos.security.filter;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.server.ClassAndMethod;
import nafos.server.SpringApplicationContextHolder;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 黄新宇
 * @Date 2018/5/17 下午3:24
 * @Description 开机自动注册session到期附带更改方法
 **/
public class SessionTimeUpdateFactory {

    public static List<ClassAndMethod> handles = new ArrayList<>();

    public static void init() {
        ApplicationContext context = SpringApplicationContextHolder.getContext();
        String[] filterNames = context.getBeanNamesForType(SessionTimeUpdate.class);
        for (String filterName : filterNames) {
            MethodAccess filterMa = MethodAccess.get(context.getType(filterName));
            int index = 0;
            handles.add(new ClassAndMethod(context.getType(filterName), filterMa, index));
        }
    }


}
