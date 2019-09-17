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

    protected String[] interceptorParams;//拦截器参数

    public RouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, Protocol type, boolean runOnWorkGroup, Class[] interceptors,String[] interceptorParams) {
        super(clazz, method, index, paramType);
        this.printLog = printLog;
        this.type = type;
        this.runOnWorkGroup = runOnWorkGroup;
        this.interceptors = interceptors;
        this.interceptorParams = interceptorParams;
    }

    public RouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, boolean printLog, Protocol type, boolean runOnWorkGroup, Class[] interceptors) {
        super(clazz, method, index, paramType);
        this.printLog = printLog;
        this.type = type;
        this.runOnWorkGroup = runOnWorkGroup;
        this.interceptors = interceptors;
    }

    public RouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, boolean printLog, Protocol type, boolean runOnWorkGroup, Class[] interceptors,String[] interceptorParams) {
        super(clazz, method, index);
        this.printLog = printLog;
        this.type = type;
        this.runOnWorkGroup = runOnWorkGroup;
        this.interceptors = interceptors;
        this.interceptorParams = interceptorParams;
    }

    public RouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, boolean printLog, Protocol type, boolean runOnWorkGroup, Class[] interceptors) {
        super(clazz, method, index);
        this.printLog = printLog;
        this.type = type;
        this.runOnWorkGroup = runOnWorkGroup;
        this.interceptors = interceptors;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public MethodAccess getMethod() {
        return method;
    }

    @Override
    public void setMethod(MethodAccess method) {
        this.method = method;
    }

    @Override
    public Integer getIndex() {
        return index;
    }

    @Override
    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public Class<?> getParamType() {
        return paramType;
    }

    @Override
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

    public String[] getInterceptorParams() {
        return interceptorParams;
    }

    public void setInterceptorParams(String[] interceptorParams) {
        this.interceptorParams = interceptorParams;
    }
}
