package nafos.security;

import nafos.core.mode.runner.NafosRunner;

import java.util.concurrent.TimeUnit;

public class ScurityRunner implements NafosRunner {
    @Override
    public void run() {
        // 1.注册定时清理cacheMap
        FiberDo.Companion.doClearTimeOutSession(TimeUnit.HOURS.toMillis(2));
    }
}
