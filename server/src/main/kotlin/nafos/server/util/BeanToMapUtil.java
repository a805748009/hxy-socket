package nafos.server.util;

import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanToMapUtil {

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = beanClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Map<String, Object> beanToMap(Object obj) {
        return BeanMap.create(obj);
    }
}
