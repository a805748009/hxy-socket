package nafos.game.manager;

import nafos.bootStrap.handle.currency.ExcuteHandle;
import nafos.bootStrap.handle.socket.SocketExecutorPoolChoose;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.runner.NafosRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/10/15 下午3:40
 * @Description 修改socket路由启动方式
 **/
@Component
public class GameSocketRouteHandleInitialize implements NafosRunner {
    private static final Logger logger = LoggerFactory.getLogger(GameSocketRouteHandleInitialize.class);

    @Override
    public void run() {
        logger.info("socket初始化修改routeHandle为：{}","GameSocketRouteHandle");
        ExcuteHandle.setAbstractSocketRouteHandle(SpringApplicationContextHolder.getSpringBeanForClass(GameSocketRouteHandle.class));
        SocketExecutorPoolChoose.abstractSocketRouteHandle = SpringApplicationContextHolder.getSpringBeanForClass(GameSocketRouteHandle.class);
    }
}
