package nafos.core.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/10/8 下午1:48
 * @Description TODO
 **/
public class GsonUtil {
    private static Gson gson = null;
    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    /**
     * 对象转成json
     *
     * @param object
     * @return json
     */
    public static String gsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * Json转成对象
     *
     * @param gsonString
     * @param cls
     * @return 对象
     */
    public static <T> T gsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * json转成list<T>
     *
     * @param gsonString
     * @param cls
     * @return list<T>
     */
    public static <T> List<T> gsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * json转成list中有map的
     *
     * @param gsonString
     * @return List<Map<String, T>>
     */
    public static <T> List<Map<String, T>> gsonToMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
            }.getType());
        }
        return list;
    }

    /**
     * json转成map的
     *
     * @param gsonString
     * @return Map<String, T>
     */
    public static <T> Map<String, T> gsonToMap(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
