package nafos.core.shutdown;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.entry.ClassAndMethod;
import nafos.core.mode.InitMothods;
import nafos.core.util.SpringApplicationContextHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:20
 * @Description TODO
 **/
@Component
public class ShutDownFilterInit implements CommandLineRunner {

    private ClassAndMethod classAndMethod;

    public void setShutDownFilterInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(ShutDownFilter.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("run");
            classAndMethod =  new ClassAndMethod(context.getType(filterNames[0]), filterMa, index);
            InitMothods.setFilter("nafosServerShutDown",classAndMethod);
        }
    }


    @Override
    public void run(String... strings) throws Exception {
        setShutDownFilterInit(SpringApplicationContextHolder.getContext());
    }
}
