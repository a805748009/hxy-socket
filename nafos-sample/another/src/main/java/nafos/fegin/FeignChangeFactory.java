//package nafos.remote.feign;
//
//import nafos.core.annotation.controller.Controller;
//import nafos.core.annotation.controller.Handle;
//import nafos.core.annotation.rpc.RemoteCall;
//import nafos.core.util.SpringApplicationContextHolder;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.MethodIntrospector;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @Author 黄新宇
// * @Date 2018/10/30 下午4:50
// * @Description 动态修改所有被@FeignClient注解的类
// **/
//@Component
//public class FeignChangeFactory implements ApplicationRunner {
//
//    @Override
//    public void run(ApplicationArguments applicationArguments) throws Exception {
//        ApplicationContext context = SpringApplicationContextHolder.getContext();
//        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(FeignClient.class);
//
//        //2.2 遍历判断，合格的注册到map
//        taskBeanMap.keySet().forEach(beanName -> {
//            try {
//                detectHandlerMethods(context.getType((String) beanName));
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        });
//
//
//    }
//
//    /**
//     * 根据类遍历方法
//     * @param handlerType
//     */
//    private void detectHandlerMethods(final Class<?> handlerType) throws NoSuchFieldException, IllegalAccessException {
//
//        //获取类的父类，此处没有，返回的本身类
//        final Class<?> userType = ClassUtils.getUserClass(handlerType);
//
//
//        FeignClient feignClient = userType.getAnnotation(FeignClient.class);
//                //AnnotationUtils.findAnnotation(userType,FeignClient.class);
//
//        String newPath =  "/nafosRemoteCall"+feignClient.path();
//
//        //获取 feignClient 这个代理实例所持有的 InvocationHandler
//        InvocationHandler h = Proxy.getInvocationHandler(feignClient);
//        // 获取 AnnotationInvocationHandler 的 path 字段
//        for (Field field : h.getClass().getDeclaredFields()) {
//            System.out.println(field.getName()+"----");
//        }
//        Field field = h.getClass().getDeclaredField("memberValues");
//
//        // 因为这个字段事 private final 修饰，所以要打开权限
//        field.setAccessible(true);
//        // 获取 memberValues
//        Map memberValues = (Map) field.get(h);
//        // 修改 value 属性值
//        memberValues.put("path", newPath);
//
//        // 获取 foo 的 value 属性值
//        String value = feignClient.value();
//        System.out.println("-----"+value);
//
//
//    }
//}
