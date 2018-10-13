package nafos.core.entry.http;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import nafos.core.util.AESUtil;
import nafos.core.util.SnowflakeIdWorker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月25日 下午5:52:20 
* 类说明 
*/
@Component
public class NafosRespone {

	private Object object;	//参数
	
	private List<Cookie> cookies = new ArrayList<Cookie>();//cookie
	
	private Cookie goSession;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public List<Cookie> getCookies() {
		return cookies;
	}

	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}

	
	public String setCookie() {
		String sessionId = String.valueOf(SnowflakeIdWorker.getSnowflakeIdWorker().nextId());
		try {
			sessionId = AESUtil.encrypt(sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		goSession = new DefaultCookie("nafosCookie",sessionId );
		cookies.add(goSession);
		return sessionId;
	}
	
	
}
