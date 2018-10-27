package nafos.remote.feign;

import feign.Response;
import nafos.core.entry.BusinessException;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 黄新宇
 * @Date 2018/10/27 下午6:49
 * @Description TODO
 **/
@Configuration
public class MyErrorDecoder implements feign.codec.ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return new BusinessException(response.status(), response.reason());
    }
}
