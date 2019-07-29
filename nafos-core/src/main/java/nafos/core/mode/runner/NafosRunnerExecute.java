package nafos.core.mode.runner;

import nafos.core.helper.SpringApplicationContextHolder;
import org.springframework.context.ApplicationContext;

/**
 * 执行开机启动
 */
public class NafosRunnerExecute {

    public void execute() {
        ApplicationContext context = SpringApplicationContextHolder.getContext();
        String[] filterNames = context.getBeanNamesForType(NafosRunner.class);
        for (String filterName : filterNames) {
            ((NafosRunner) context.getBean(filterName)).run();
        }
    }
}
