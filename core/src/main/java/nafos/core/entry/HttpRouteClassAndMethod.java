package nafos.core.entry;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.Enums.Protocol;

import java.lang.reflect.Parameter;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月9日 下午5:17:02
 * 类说明
 */
public class HttpRouteClassAndMethod extends RouteClassAndMethod {

    protected Parameter[] parameters;//是否需要request



    public HttpRouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, Protocol type, boolean runOnWorkGroup, Parameter[] parameters,Class[] interceptors) {
        super(clazz, method, index, paramType, printLog, type, runOnWorkGroup,interceptors);
        this.parameters = parameters;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }


}
