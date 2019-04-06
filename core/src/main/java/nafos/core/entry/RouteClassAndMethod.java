package nafos.core.entry;


import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.Enums.Protocol;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月9日 下午5:17:02
 * 类说明
 */
public class RouteClassAndMethod extends ClassAndMethod {


    protected boolean printLog;//是否打印方法时间

    protected Protocol type; //JSON 或者 PRO[TOBUFF]

    protected boolean runOnWorkGroup;

    protected Class[] interceptors;//拦截器

    public RouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, Protocol type, boolean runOnWorkGroup, Class[] interceptors) {
        super(clazz, method, index, paramType);
        this.printLog = printLog;
        this.type = type;
        this.runOnWorkGroup = runOnWorkGroup;
        this.interceptors = interceptors;
    }

    public RouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, boolean printLog, Protocol type, boolean runOnWorkGroup, Class[] interceptors) {
        super(clazz, method, index);
        this.printLog = printLog;
        this.type = type;
        this.runOnWorkGroup = runOnWorkGroup;
        this.interceptors = interceptors;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public MethodAccess getMethod() {
        return method;
    }

    public void setMethod(MethodAccess method) {
        this.method = method;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Class<?> getParamType() {
        return paramType;
    }

    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }

    public boolean isPrintLog() {
        return printLog;
    }

    public void setPrintLog(boolean printLog) {
        this.printLog = printLog;
    }

    public Protocol getType() {
        return type;
    }

    public void setType(Protocol type) {
        this.type = type;
    }

    public boolean isRunOnWorkGroup() {
        return runOnWorkGroup;
    }

    public void setRunOnWorkGroup(boolean runOnWorkGroup) {
        this.runOnWorkGroup = runOnWorkGroup;
    }

    public Class[] getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Class[] interceptors) {
        this.interceptors = interceptors;
    }
}
