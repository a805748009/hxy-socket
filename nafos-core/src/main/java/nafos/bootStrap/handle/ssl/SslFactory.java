package nafos.bootStrap.handle.ssl;


import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.IdentityCipherSuiteFilter;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.Arrays;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年3月26日 下午7:05:15 SSL初始化
 */
public class SslFactory {
    private static Logger logger = LoggerFactory.getLogger(SslFactory.class);


    private static SslContext sslContext = null;

    public static final String[] CIPHER_ARRAY = {"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256"};

    public static SslContext createSslContext(String certFilePath, String keyFilePath) {
        if (null == sslContext) {
            synchronized (SslFactory.class) {
                if (null == sslContext) {
                    File certFile = new File(certFilePath);
                    File keyFile = new File(keyFilePath);//此处需要PKS8编码的.key后缀文件
                    try {
                        sslContext = SslContextBuilder.forServer(certFile, keyFile)
                                .clientAuth(ClientAuth.NONE).ciphers(Arrays.asList(CIPHER_ARRAY), IdentityCipherSuiteFilter.INSTANCE_DEFAULTING_TO_SUPPORTED_CIPHERS)//只允许用上面的三种128位加密套件，一般情况下去除这一行
                                .build();
                    } catch (SSLException e) {
                        logger.error("SSL错误：" + e.toString());
                    }
                }
            }
        }
        return sslContext;
    }

}
