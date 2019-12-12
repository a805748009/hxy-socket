package nafos.security;

import nafos.security.filter.SessionTimeUpdateFactory;
import nafos.server.start.NafosRunner;
import org.springframework.stereotype.Component;


@Component
public class ScurityRunner implements NafosRunner {
    @Override
    public void run() {
        // 1.注册sessionUpdateHandle,在刷新用户session时间时调用
        SessionTimeUpdateFactory.init();
    }
}
