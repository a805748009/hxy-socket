package nafos.server;


import nafos.server.handle.http.ThreadInfo;
import nafos.server.handle.http.NsRequest;
import nafos.server.handle.http.NsRespone;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月26日 下午3:47:32
 * 类说明
 */
public class ThreadLocalHelper {
    public static ThreadLocal<ThreadInfo> threadLocal = new ThreadLocal<ThreadInfo>();

    public static ThreadInfo getThreadInfo() {
        return threadLocal.get();
    }

    public static NsRequest getRequest() {
        return threadLocal.get().getNafosRequest();
    }

    public static NsRespone getRespone() {
        return threadLocal.get().getNafosRespone();
    }

    public static void threadLocalRemove() {
        threadLocal.remove();
    }

}
