package nafos.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanToMapUtil {

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
        if (map == null)
            return null;
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

    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }
}
