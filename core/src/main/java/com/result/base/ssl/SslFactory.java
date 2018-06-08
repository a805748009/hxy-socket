package com.result.base.ssl;

import java.io.File;

import com.result.base.config.ConfigForSSL;

import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年3月26日 下午7:05:15 SSL初始化
 */
public class SslFactory {
	private static  SslContext sslContext = null;

	public static SslContext createSSLContext() throws Exception {
		if (null == sslContext) {
			synchronized (SslFactory.class) {
				if (null == sslContext) {
					 File certFilePath = new File(ConfigForSSL.CERTFILEPATH);
					 File keyFilePath =  new File(ConfigForSSL.KEYFILEPATH);//此处需要PKS8编码的.key后缀文件
					 sslContext = SslContextBuilder.forServer(certFilePath, keyFilePath).clientAuth(ClientAuth.NONE).build();
				}
			}
		}
		return sslContext;
	}

}
