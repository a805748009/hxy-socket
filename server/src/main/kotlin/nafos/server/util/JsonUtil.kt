package nafos.server.util

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.LoggerFactory
import java.io.IOException
import java.text.SimpleDateFormat

object JsonUtil {
    private val log = LoggerFactory.getLogger(JsonUtil::class.java)

    private var objectMapper: ObjectMapper = ObjectMapper()

    private var objectMapperNotNull: ObjectMapper = ObjectMapper()

    private val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    init {
        /**
         *  初始化
         *  jackson版本的json
         */
        objectMapper.apply {
            setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE;
            dateFormat = fmt;
            registerKotlinModule()
        }


        /**
         * 不序列化为null的属性
         * 只对实体对象起作用，Map List不起作用
         * 通过该方法对mapper对象进行设置，所有序列化的对象都将按该规则进行系列化
         * Include.Include.ALWAYS 默认
         * Include.NON_DEFAULT 属性为默认值不序列化
         * Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
         * Include.NON_NULL 属性为NULL 不序列化
         * dateFormat jackson对日期格式的处理
         */
        objectMapperNotNull.apply {
            setSerializationInclusion(JsonInclude.Include.NON_NULL);
            setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE;
            dateFormat = fmt;
            registerKotlinModule()
        }
    }

    fun getObjectMapper(): ObjectMapper {
        return objectMapper
    }

    fun getObjectMapperNotNull(): ObjectMapper {
        return objectMapperNotNull
    }

    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
     *
     * @param <T>
     * @param jsonString JSON字符串
     * @param tr         TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @return List对象列表
    </FamousUser></T> */
    fun <T> json2GenericObject(jsonString: String?, tr: TypeReference<T>): T? {
        if (jsonString.isNullOrBlank()) {
            return null
        } else {
            try {
                return objectMapper.readValue(jsonString, tr)
            } catch (e: Exception) {
                log.warn("json error:" + e.message, e)
            }

        }
        return null
    }

    /**
     * Json字符串转Java对象（jackson方式）
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    fun <T> json2Object(jsonString: String?, clazz: Class<T>): T? {
        if (jsonString == null || "" == jsonString) {
            return null
        }
        try {
            return objectMapper.readValue(jsonString, clazz)
        } catch (e: Exception) {
            log.warn("json error:" + e.message, e)
        }
        return null
    }


    /**
     * 根据json串和节点名返回节点
     *
     * @param json
     * @param nodeName
     * @return
     */
    fun getNode(json: String, nodeName: String): JsonNode? {
        var node: JsonNode? = null
        try {
            node = JsonUtil.getObjectMapper().readTree(json)
            return node!!.get(nodeName)
        } catch (e: JsonProcessingException) {
            log.warn("json error:" + e.message, e)
        } catch (e: IOException) {
            log.warn("json error:" + e.message, e)
        }
        return node
    }

    /**
     * 将Java对象转换成Json字符串
     *
     * @param object Java对象，可以是对象，数组，List,Map等
     * @return json 字符串
     */
    fun toJson(`object`: Any): String {
        if (`object` is String) {
            return `object`
        }
        var jsonString = ""
        try {
            jsonString = objectMapper.writeValueAsString(`object`)
        } catch (e: Exception) {
            log.warn("json error:" + e.message, e)
        }
        return jsonString

    }

    /**
     * 将Java对象转换成Json字符串 如果对象中的属性值为null 则不在字符串中显示该属性，只对实体对象起作用，Map List不起作用
     * @param object
     * @return
     * @author HXY
     * @date 2017年8月21日下午1:49:47
     */
    fun toJsonIsNotNull(`object`: Any): String {
        var jsonString = ""
        try {
            /**
             * 只对实体对象起作用，Map List不起作用
             * 通过该方法对mapper对象进行设置，所有序列化的对象都将按该规则进行系列化
             * Include.Include.ALWAYS 默认
             * Include.NON_DEFAULT 属性为默认值不序列化
             * Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
             */
            jsonString = objectMapperNotNull.writeValueAsString(`object`)
        } catch (e: Exception) {
            log.warn("json error:" + e.message, e)
        }
        return jsonString
    }

    /**
     * 将json字符串转换成列表
     * @param json
     * @return
     * @author HXY
     * @date 2017年8月21日下午1:49:15
     */
    fun jsonToList(json: String): List<*>? {
        try {
            return objectMapper.readValue(json, List::class.java)
        } catch (e: Exception) {
            log.warn("json error:" + e.message, e)
        }
        return null
    }

    /**
     * 将json字符串转换成Map
     * @param json
     * @return
     * @author HXY
     * @date 2017年8月21日下午1:49:33
     */
    fun jsonToMap(json: String): Map<String, Any> {
        try {
            return objectMapper.readValue(json, Map::class.java) as Map<String, Any>
        } catch (e: Exception) {
            log.warn("json error:" + e.message, e)
        }
        return mapOf()
    }

}