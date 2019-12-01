package nafos.server;


import nafos.server.handle.http.CoroutineInfo;
import nafos.server.handle.http.NsRequest;
import nafos.server.handle.http.NsRespone;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月26日 下午3:47:32
 * 类说明
 */
public class CoroutineLocalHelper {
    public static ThreadLocal<CoroutineInfo> coroutineLocal = new ThreadLocal<CoroutineInfo>();

    public static CoroutineInfo getCoroutineInfo() {
        return coroutineLocal.get();
    }

    public static NsRequest getRequest() {
        return coroutineLocal.get().getNafosRequest();
    }

    public static NsRespone getRespone() {
        return coroutineLocal.get().getNafosRespone();
    }

    public static void coroutineLocalRemove() {
        coroutineLocal.remove();
    }

}
