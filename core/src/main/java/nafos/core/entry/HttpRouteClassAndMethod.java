package nafos.core.entry;

import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Parameter;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月9日 下午5:17:02 
* 类说明 
*/
public class HttpRouteClassAndMethod extends  RouteClassAndMethod {

	protected Parameter[] parameters;//是否需要request

	public HttpRouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, String type, boolean runOnWorkGroup, Parameter[] parameters) {
		super(clazz, method, index, paramType, printLog, type, runOnWorkGroup);
		this.parameters = parameters;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}
}
