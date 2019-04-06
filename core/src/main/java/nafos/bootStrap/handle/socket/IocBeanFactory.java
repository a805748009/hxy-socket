package nafos.bootStrap.handle.socket;

/**
 * @Author 黄新宇
 * @Date 2018/10/15 下午3:34
 * @Description IOC容器中路由实例化的handle
 **/
public class IocBeanFactory {

    private static String socketRouthandle = "SocketRouteHandle";

    public static void updateSocketRouthandle(String name) {
        socketRouthandle = name;
    }

    public static String getSocketRouthandle() {
        return socketRouthandle;
    }
}
