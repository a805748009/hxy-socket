package hxy.server.socket.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper;

    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        if(SpringApplicationContextHolder.containsBean("objectMapper")){
            objectMapper = SpringApplicationContextHolder.getBean("objectMapper");
        }else{
            objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            // objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
            // jackson对日期格式的处理
            objectMapper.setDateFormat(fmt);
        }
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
     *
     * @param <T>
     * @param jsonString JSON字符串
     * @param tr         TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @return List对象列表
     */
    @SuppressWarnings("unchecked")
    public static <T> T json2GenericObject(String jsonString, TypeReference<T> tr) {
        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return (T) objectMapper.readValue(jsonString, tr);
            } catch (Exception e) {
                log.warn("json error-jsonString:{}" ,jsonString);
            }
        }
        return null;
    }

    /**
     * Json字符串转Java对象（jackson方式）
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T json2Object(String jsonString, Class<T> clazz) {
        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(jsonString, clazz);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("json error- {} {}", jsonString, clazz);
            }

        }
        return null;
    }


    /**
     * 根据json串和节点名返回节点
     *
     * @param json
     * @param nodeName
     * @return
     */
    public static JsonNode getNode(String json, String nodeName) {
        JsonNode node = null;
        try {
            node = JsonUtil.getObjectMapper().readTree(json);
            return node.get(nodeName);
        } catch (IOException e) {
            log.warn("json error- json:{} nodeName:{}", json, nodeName);
        }
        return null;
    }

    /**
     * 将Java对象转换成Json字符串
     *
     * @param object Java对象，可以是对象，数组，List,Map等
     * @return json 字符串
     */
    public static String toJson(Object object) {
        if (object instanceof String) {
            return (String) object;
        }
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("json error- {}", object.toString());
        }
        return jsonString;

    }



    /***
     * @Description: 将json字符串转换成列表
     * @author hxy
     * @date 2020/4/17 15:26
     */
    public static List<?> jsonToList(String json) {
        try {
            return (List<?>) objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            log.warn("json error- {}", json);
        }
        return null;
    }

    /***
     * @Description: 将json字符串转换成Map
     * @author hxy
     * @date 2020/4/17 15:25
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String json) {
        try {
            return (Map<String, Object>) objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.warn("json error- {}", json);
        }
        return null;
    }

}
