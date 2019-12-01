package nafos.server

import com.esotericsoftware.reflectasm.MethodAccess
import nafos.server.enums.Protocol
import java.lang.reflect.Parameter

/**
 * @Description
 * @Author      xinyu.huang
 * @Time        2019/11/23 21:05
 */
class HttpRouteClassAndMethod: RouteClassAndMethod {
    /**
     * 是否需要request
     */
    var parameters: Array<Parameter> = emptyArray()


    constructor(clazz: Class<*>, method: MethodAccess, index: Int?, paramType: Array<Class<*>>?, type: Protocol,
                parameters: Array<Parameter>, interceptors: Array<Class<*>>, interceptorParams: Array<String>)
            : super(clazz, method, index, paramType, type, interceptors, interceptorParams){

        this.parameters = parameters
    }
}