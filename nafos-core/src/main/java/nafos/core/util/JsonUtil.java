package nafos.core.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json 工具类
 *
 * @author HXY
 * @date 2017年8月21日下午1:48:33
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper;

    private static final ObjectMapper objectMapperNotNull;

    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        // 初始化
        objectMapper = new ObjectMapper();// jackson版本的json
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        // objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        // jackson对日期格式的处理
        objectMapper.setDateFormat(fmt);

        // 不序列化为null的属性
        // 只对实体对象起作用，Map List不起作用
        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按该规则进行系列化
        // Include.Include.ALWAYS 默认
        // Include.NON_DEFAULT 属性为默认值不序列化
        // Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
        // Include.NON_NULL 属性为NULL 不序列化
        objectMapperNotNull = new ObjectMapper();
        objectMapperNotNull.setSerializationInclusion(Include.NON_NULL);
        objectMapperNotNull.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        objectMapperNotNull.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapperNotNull.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperNotNull.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        // jackson对日期格式的处理
        objectMapperNotNull.setDateFormat(fmt);

    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    public static ObjectMapper getObjectMapperNotNull() {
        return objectMapperNotNull;
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
                log.warn("json error:" + e.getMessage(), e);
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
                log.warn("json error:" + e.getMessage(), e);
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
        } catch (JsonProcessingException e) {
            log.warn("json error:" + e.getMessage(), e);
        } catch (IOException e) {
            log.warn("json error:" + e.getMessage(), e);
        }
        return node;
    }

    /**
     * 将Java对象转换成Json字符串
     *
     * @param object Java对象，可以是对象，数组，List,Map等
     * @return json 字符串
     */
    public static String toJson(Object object) {
        if (object instanceof String) return (String) object;
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("json error:" + e.getMessage(), e);
        }
        return jsonString;

    }

    /**
     * 将Java对象转换成Json字符串 如果对象中的属性值为null 则不在字符串中显示该属性，只对实体对象起作用，Map List不起作用
     *
     * @param object
     * @return
     * @author HXY
     * @date 2017年8月21日下午1:49:47
     */
    public static String toJsonIsNotNull(Object object) {
        String jsonString = "";
        try {
            // 只对实体对象起作用，Map List不起作用
            // 通过该方法对mapper对象进行设置，所有序列化的对象都将按该规则进行系列化
            // Include.Include.ALWAYS 默认
            // Include.NON_DEFAULT 属性为默认值不序列化
            // Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
            // Include.NON_NULL 属性为NULL 不序列化
            // ObjectMapper mapper = new ObjectMapper();
            jsonString = objectMapperNotNull.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("json error:" + e.getMessage(), e);
        }
        return jsonString;
    }

    /**
     * 将json字符串转换成列表
     *
     * @param json
     * @return
     * @author HXY
     * @date 2017年8月21日下午1:49:15
     */
    public static List<?> jsonToList(String json) {
        try {
            return (List<?>) objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            log.warn("json error:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将json字符串转换成Map
     *
     * @param json
     * @return
     * @author HXY
     * @date 2017年8月21日下午1:49:33
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String json) {
        try {
            // ObjectMapper mapper = new ObjectMapper();
            return (Map<String, Object>) objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.warn("json error:" + e.getMessage(), e);
        }
        return null;
    }


}
