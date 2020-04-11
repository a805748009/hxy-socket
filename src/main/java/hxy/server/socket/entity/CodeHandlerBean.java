package hxy.server.socket.entity;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @Description code处理
 * @Author xinyu.huang
 * @Time 2020/4/11 14:30
 */
public class CodeHandlerBean {

    /**
     * 类
     */
    private Class<?> clazz;

    /**
     * 方法
     */
    private MethodAccess method;

    /**
     * 实例化方法的index
     */
    private Integer index;

    /**
     * 参数类型class
     */
    private Class<?>[] paramType;

    private Object bean;

    public CodeHandlerBean(Class<?> clazz, MethodAccess method, Integer index, Class<?>[] paramType, Object bean) {
        this.clazz = clazz;
        this.method = method;
        this.index = index;
        this.paramType = paramType;
        this.bean = bean;
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

    public Class<?>[] getParamType() {
        return paramType;
    }

    public void setParamType(Class<?>[] paramType) {
        this.paramType = paramType;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
