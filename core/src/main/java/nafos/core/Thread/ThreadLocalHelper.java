package nafos.core.Thread;


import nafos.core.entry.http.NafosRequest;
import nafos.core.entry.http.NafosRespone;
import nafos.core.entry.http.NafosThreadInfo;

/**
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月26日 下午3:47:32 
* 类说明 
*/
public class ThreadLocalHelper {
    private static java.lang.ThreadLocal<NafosThreadInfo> map = new java.lang.ThreadLocal<NafosThreadInfo>();
	
    
	public static void setThreadInfo(NafosThreadInfo nafosThreadInfo){
		map.set(nafosThreadInfo);
	}
	
	public static NafosThreadInfo getThreadInfo(){
		return map.get();
	}
	
	public static NafosRequest getRequest(){
		return map.get().getNafosRequest();
	}
	     
	public static NafosRespone getRespone(){
		return map.get().getNafosRespone();
	}

	public static void threadLocalRemove(){
		map.remove();
	}
	    
}
