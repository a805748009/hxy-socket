package com.mode.configuration;

import feign.codec.Decoder;
import feign.codec.Encoder;
import nafos.remote.feign.ProtostuffHttpMessageConverter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 黄新宇
 * @Date 2018/10/30 上午11:59
 * @Description TODO
 **/
@Configuration
public class ProtoFeignConfiguration {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverterObjectFactory;
    @Bean
    public ProtostuffHttpMessageConverter protostufHttpMessageConverter() {
        return new ProtostuffHttpMessageConverter();
    }

    @Bean
    public Encoder springEncoder() {
        return new SpringEncoder(this.messageConverterObjectFactory);
    }

    @Bean
    public Decoder springDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(this.messageConverterObjectFactory));
    }
}


