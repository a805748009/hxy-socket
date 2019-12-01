package nafos.server

import com.esotericsoftware.reflectasm.MethodAccess
import nafos.server.enums.Protocol

/**
 * @Author      xinyu.huang
 * @Time        2019/11/23 20:40
 */
open class RouteClassAndMethod : ClassAndMethod {
    /**
     * JSON 或者 PROTO_STUFF
     */
    var type: Protocol

    /**
     * 拦截器
     */
    var interceptors: Array<Class<*>>

    /**
     * 拦截器参数
     */
    var interceptorParams: Array<String> = emptyArray()

    constructor(clazz: Class<*>, method: MethodAccess, index: Int?, paramType: Array<Class<*>>?, type: Protocol,
                interceptors: Array<Class<*>>, interceptorParams: Array<String>) : super(clazz, method, index, paramType) {
        this.type = type
        this.interceptors = interceptors
        this.interceptorParams = interceptorParams
    }

    constructor(clazz: Class<*>, method: MethodAccess, index: Int?, paramType: Array<Class<*>>?,
                type: Protocol, interceptors: Array<Class<*>>) : super(clazz, method, index, paramType) {
        this.type = type
        this.interceptors = interceptors
    }

    constructor(clazz: Class<*>, method: MethodAccess, index: Int?, type: Protocol,
                interceptors: Array<Class<*>>, interceptorParams: Array<String>) : super(clazz, method, index) {
        this.type = type
        this.interceptors = interceptors
        this.interceptorParams = interceptorParams
    }

    constructor(clazz: Class<*>, method: MethodAccess, index: Int?, type: Protocol, runOnWorkGroup: Boolean, interceptors: Array<Class<*>>) : super(clazz, method, index) {
        this.type = type
        this.interceptors = interceptors
    }
}