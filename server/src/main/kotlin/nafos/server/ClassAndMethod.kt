package nafos.server

import com.esotericsoftware.reflectasm.MethodAccess

/**
 * @Author      xinyu.huang
 * @Time        2019/11/23 20:08
 */
open class ClassAndMethod {
    /**
     * 类
     */
    var clazz: Class<*>

    /**
     * 方法
     */
    var method: MethodAccess

    /**
     * 实例化方法的index
     */
    var index: Int? = null

    /**
     * 参数类型class
     */
    var paramType: Array<Class<*>>? = null

    constructor(clazz: Class<*>, method: MethodAccess, index: Int?, paramType: Array<Class<*>>?) {
        this.clazz = clazz
        this.method = method
        this.index = index
        this.paramType = paramType
    }

    constructor(clazz: Class<*>, method: MethodAccess, index: Int?) {
        this.clazz = clazz
        this.method = method
        this.index = index
        this.paramType = null
    }
}