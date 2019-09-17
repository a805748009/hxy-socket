package nafos.core.mode;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.annotation.socket.Connect;
import nafos.core.annotation.socket.DisConnect;
import nafos.core.annotation.socket.SocketActive;
import nafos.core.entry.ClassAndMethod;
import nafos.core.helper.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author 黄新宇
 * @Date 2018/10/10 下午3:28
 * @Description TODO
 **/
public class SocketConnectFactory {
    private static final Logger logger = LoggerFactory.getLogger(SocketConnectFactory.class);

    private final List<ClassAndMethod> connectClassAndMethod = new ArrayList<>();

    private List<ClassAndMethod> disConnectClassAndMethod = new ArrayList<>();


    public SocketConnectFactory(ApplicationContext context) {
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(SocketActive.class);
        TreeSet socketActiveHandles = new TreeSet(new TreeSetComparator());
        socketActiveHandles.addAll(taskBeanMap.keySet());
        Object[] names =  socketActiveHandles.toArray();
        if(names.length>0){
            for(int i = 0;i<names.length;i++){
                detectHandlerMethods(context.getType((String) names[i]));
            }
        }
    }

    public  List<ClassAndMethod> getConnectClassAndMethod(){
        return  connectClassAndMethod;
    }

    public  List<ClassAndMethod> getDisConnectClassAndMethod(){
        return  disConnectClassAndMethod;
    }


    /**
     * 根据类遍历方法，拼接后注册实际操作方法
     * @param handlerType
     */
    private void detectHandlerMethods(final Class<?> handlerType) {

        //获取类的父类，此处没有，返回的本身类
        final Class<?> userType = ClassUtils.getUserClass(handlerType);

        Set<Method> methods1 = MethodIntrospector.selectMethods(userType, new ReflectionUtils.MethodFilter(){
            @Override
            public boolean matches(Method method) {
                return method.isAnnotationPresent(Connect.class);
            }
        });

        Set<Method> methods2 = MethodIntrospector.selectMethods(userType, new ReflectionUtils.MethodFilter(){
            @Override
            public boolean matches(Method method) {
                return method.isAnnotationPresent(DisConnect.class);
            }
        });

        if(!methods1.isEmpty()) {
            connectClassAndMethod.add(registerHandlerMethod((Method) methods1.toArray()[0], userType));
        }

        if(!methods2.isEmpty()) {
            disConnectClassAndMethod.add(registerHandlerMethod((Method) methods2.toArray()[0], userType));
        }

    }


    private ClassAndMethod registerHandlerMethod(Method method, Class<?> handlerType) {
        logger.debug("加载socket连接或断开连接事件：{}",handlerType.toString()+"."+method.getName());
        MethodAccess ma = MethodAccess.get(handlerType);
        return new ClassAndMethod(handlerType,ma,ma.getIndex(method.getName()));
    }

    public class TreeSetComparator implements Comparator<String>{
        @Override
        public int compare(String o1, String o2) {
            SocketActive socketActive1 = AnnotationUtils.findAnnotation(SpringApplicationContextHolder.getSpringBean(o1).getClass(),SocketActive.class);
            SocketActive socketActive2 = AnnotationUtils.findAnnotation(SpringApplicationContextHolder.getSpringBean(o2).getClass(),SocketActive.class);
            return socketActive1.index()-socketActive2.index();
        }
    }
}
