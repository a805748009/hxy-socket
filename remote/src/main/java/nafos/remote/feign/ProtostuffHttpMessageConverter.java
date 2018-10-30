package nafos.remote.feign;

import nafos.core.util.ProtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author 黄新宇
 * @Date 2018/10/30 下午1:24
 * @Description feign调用中protostuff编解码器
 **/
public class ProtostuffHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    private static final Logger logger = LoggerFactory.getLogger(ProtostuffHttpMessageConverter.class);

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final MediaType PROTOBUF;

    static {
        PROTOBUF = new MediaType("application", "x-protobuf", DEFAULT_CHARSET);
    }

    public ProtostuffHttpMessageConverter() {
        super(new MediaType[]{PROTOBUF, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML});
    }


    @Override
    protected boolean supports(Class<?> aClass) {
       return Object.class.isAssignableFrom(aClass);
    }

    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        if (contentType == null) {
            contentType = PROTOBUF;
        }
        if (!PROTOBUF.isCompatibleWith(contentType)) {
            logger.error("不支持的解码格式，请用x-protobuf作为contentType");
        }

        try {
            return ProtoUtil.deserializeFromByte(inputMessage.getBody(),aClass);
        } catch (Exception var7) {
            throw new HttpMessageNotReadableException("Could not read Protobuf message: " + var7.getMessage(), var7);
        }
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        if (contentType == null) {
            contentType = PROTOBUF;
        }

        Charset charset = contentType.getCharset();
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }

        if (!PROTOBUF.isCompatibleWith(contentType)) {
            logger.error("不支持的编码格式，请用x-protobuf作为contentType");
        }


        FileCopyUtils.copy(ProtoUtil.serializeToByte(object), outputMessage.getBody());

    }
}
