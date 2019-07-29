package nafos.core.entry;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.Enums.Protocol;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月9日 下午5:17:02
 * 类说明
 */
public class SocketRouteClassAndMethod extends RouteClassAndMethod {

    public SocketRouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, Protocol type, boolean runOnWorkGroup,Class[] interceptors) {
        super(clazz, method, index, paramType, printLog, type, runOnWorkGroup,interceptors);
    }
}
