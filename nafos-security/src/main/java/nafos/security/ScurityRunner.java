package nafos.security;

import nafos.core.mode.runner.NafosRunner;
import nafos.security.filter.SessionTimeUpdateFactory;
import org.springframework.stereotype.Component;


@Component
public class ScurityRunner implements NafosRunner {
    @Override
    public void run() {
        // 1.注册sessionUpdateHandle,在刷新用户session时间时调用
        new SessionTimeUpdateFactory().init();
    }
}
