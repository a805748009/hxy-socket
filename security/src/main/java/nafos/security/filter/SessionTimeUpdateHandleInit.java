package nafos.security.filter;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.entry.ClassAndMethod;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.InitMothods;
import nafos.core.mode.runner.NafosRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/5/17 下午3:24
 * @Description 开机自动注册session到期附带更改方法
 **/
@Component
public class SessionTimeUpdateHandleInit implements NafosRunner {

    private ClassAndMethod classAndMethod;

    public void SessionTimeUpdateHandleInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(SessionTimeUpdate.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("run");
            classAndMethod =  new ClassAndMethod(context.getType(filterNames[0]), filterMa, index);
            InitMothods.setFilter("sessionUpdate",classAndMethod);
        }
    }



    @Override
    public void run() {
        SessionTimeUpdateHandleInit(SpringApplicationContextHolder.getContext());
    }
}
