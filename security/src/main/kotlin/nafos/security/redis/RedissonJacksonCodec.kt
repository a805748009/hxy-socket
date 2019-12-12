package nafos.security.redis

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import org.redisson.client.codec.BaseCodec
import org.redisson.client.codec.Codec
import org.redisson.client.protocol.Decoder
import org.redisson.client.protocol.Encoder
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.xml.datatype.XMLGregorianCalendar

/**
 *@ClassName (RedissonJacksonCodec)
 *@Desc KOTLIN-redisson-jackson-codec
 *@Author hxy
 *@Date 2019/7/25 21:31
 **/
open class RedissonJacksonCodec : BaseCodec {
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "@id")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
    class ThrowableMixIn

    private lateinit var mapObjectMapper: ObjectMapper

    private val encoder = Encoder { `in` ->
        val out = ByteBufAllocator.DEFAULT.buffer()
        try {
            val os = ByteBufOutputStream(out)
            mapObjectMapper.writeValue(os as OutputStream, `in`)
            return@Encoder os.buffer()
        } catch (e: IOException) {
            out.release()
            throw e
        }
    }

    private val decoder = Decoder { buf, _ -> mapObjectMapper.readValue(ByteBufInputStream(buf) as InputStream, Any::class.java) }

    constructor(){
        initc()
        initTypeInclusion(this.mapObjectMapper)
    }

    constructor(classLoader: ClassLoader) {
        createObjectMapper(classLoader, ObjectMapper())
    }

    fun createObjectMapper(classLoader: ClassLoader, om: ObjectMapper): ObjectMapper {
        val tf = TypeFactory.defaultInstance().withClassLoader(classLoader)
        om.typeFactory = tf
        return om
    }



    protected fun initTypeInclusion(mapObjectMapper: ObjectMapper) {
        val mapTyper = object : ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL) {
            override fun useForType(t: JavaType): Boolean {
                var t = t
                when (_appliesFor) {
                    ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS -> {
                        while (t.isArrayType) {
                            t = t.contentType
                        }
                        return t.rawClass == Any::class.java || !t.isConcrete
                    }
                    ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE -> return t.rawClass == Any::class.java || !t.isConcrete
                    ObjectMapper.DefaultTyping.NON_FINAL -> {
                        while (t.isArrayType) {
                            t = t.contentType
                        }
                        if (t.rawClass == Long::class.java) {
                            return true
                        }
                        return if (t.rawClass == XMLGregorianCalendar::class.java) {
                            false
                        } else !t.isFinal
                    }
                    else ->
                        return t.rawClass == Any::class.java
                }
            }
        }
        mapTyper.init(JsonTypeInfo.Id.CLASS, null)
        mapTyper.inclusion(JsonTypeInfo.As.PROPERTY)
        mapObjectMapper.setDefaultTyping(mapTyper)
        try {
            val s = mapObjectMapper.writeValueAsBytes(1)
            mapObjectMapper.readValue(s, Any::class.java)
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }

    }

    protected fun initc() {
        mapObjectMapper = ObjectMapper()
        mapObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapObjectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        mapObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapObjectMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        mapObjectMapper.registerKotlinModule()
    }

    override fun getMapValueDecoder(): Decoder<Any> {
        return decoder
    }

    override fun getMapValueEncoder(): Encoder {
        return encoder
    }

    override fun getMapKeyDecoder(): Decoder<Any> {
        return decoder
    }

    override fun getMapKeyEncoder(): Encoder {
        return encoder
    }

    override fun getValueDecoder(): Decoder<Any> {
        return decoder
    }

    override fun getValueEncoder(): Encoder {
        return encoder
    }

    fun getObjectMapper(): ObjectMapper {
        return mapObjectMapper
    }
}