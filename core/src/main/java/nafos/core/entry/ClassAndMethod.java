package nafos.core.entry;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @Author 黄新宇
 * @Date 2018/7/9 上午11:13
 * @Description TODO
 **/
public class ClassAndMethod {

    protected Class<?> clazz; //类

    protected MethodAccess method; //方法

    protected Integer index; //实例化方法的index

    protected Class<?> paramType;//参数类型class

    public ClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType) {
        this.clazz = clazz;
        this.method = method;
        this.index = index;
        this.paramType = paramType;
    }

    public ClassAndMethod(Class<?> clazz, MethodAccess method, Integer index) {
        this.clazz = clazz;
        this.method = method;
        this.index = index;
        this.paramType = null;
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
}
