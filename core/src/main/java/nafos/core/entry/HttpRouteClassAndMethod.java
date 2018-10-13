package nafos.core.entry;

import com.esotericsoftware.reflectasm.MethodAccess;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月9日 下午5:17:02 
* 类说明 
*/
public class HttpRouteClassAndMethod extends  RouteClassAndMethod {

	protected boolean isRequest;//是否需要request

	public HttpRouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, String type, boolean runOnWorkGroup, boolean isRequest) {
		super(clazz, method, index, paramType, printLog, type, runOnWorkGroup);
		this.isRequest = isRequest;
	}

	public boolean isRequest() {
		return isRequest;
	}

	public void setRequest(boolean request) {
		isRequest = request;
	}
}
