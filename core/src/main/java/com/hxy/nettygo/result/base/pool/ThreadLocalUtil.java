package com.hxy.nettygo.result.base.pool;

import com.hxy.nettygo.result.base.entry.GoRequest;
import com.hxy.nettygo.result.base.entry.GoRespone;
import com.hxy.nettygo.result.base.entry.GoHttpThread;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月26日 下午3:47:32 
* 类说明 
*/
public class ThreadLocalUtil {
    private static ThreadLocal<GoHttpThread> map = new ThreadLocal<GoHttpThread>();
	
    
	public static void setSession(GoHttpThread goHttpThread){
		map.set(goHttpThread);
	}
	
	public static GoHttpThread getSession(){
		return map.get();
	}
	
	public static GoRequest getRequest(){
		return map.get().getGoRequest();
	}
	     
	public static GoRespone getRespone(){
		return map.get().getGoRespone();
	}

	public static void threadLocalRemove(){
		map.remove();
	}
	    
}
