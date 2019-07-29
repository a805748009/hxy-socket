package nafos.core.Thread;


import nafos.bootStrap.handle.http.NsRequest;
import nafos.bootStrap.handle.http.NsRespone;
import nafos.core.entry.http.ReqResBean;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月26日 下午3:47:32
 * 类说明
 */
public class ThreadLocalHelper {
    private static ThreadLocal<ReqResBean> map = new ThreadLocal<ReqResBean>();


    public static void setThreadInfo(ReqResBean reqResBean) {
        map.set(reqResBean);
    }

    public static ReqResBean getThreadInfo() {
        return map.get();
    }

    public static NsRequest getRequest() {
        return map.get().getNafosRequest();
    }

    public static NsRespone getRespone() {
        return map.get().getNafosRespone();
    }

    public static void threadLocalRemove() {
        map.remove();
    }

}
