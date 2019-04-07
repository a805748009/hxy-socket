//package nafos.game.manager;
//
//import nafos.network.bootStrap.netty.NettyServer;
//import nafos.network.bootStrap.netty.handle.socket.IocBeanFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * @Author 黄新宇
// * @Date 2018/10/15 下午3:40
// * @Description 修改socket路由启动方式
// **/
//@Component
//public class GameSocketRouteHandleInitialize implements NafosRunner {
//    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
//
//    @Override
//    public void run() throws Exception {
//        logger.info("socket初始化修改routeHandle为：{}","GameSocketRouteHandle");
//        IocBeanFactory.updateSocketRouthandle("GameSocketRouteHandle");
//    }
//}
