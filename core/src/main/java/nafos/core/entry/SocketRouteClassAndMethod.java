package nafos.core.entry;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月9日 下午5:17:02 
* 类说明 
*/
public class SocketRouteClassAndMethod extends  RouteClassAndMethod{

	public SocketRouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, String type, boolean runOnWorkGroup) {
		super(clazz, method, index, paramType, printLog, type, runOnWorkGroup);
	}
}
