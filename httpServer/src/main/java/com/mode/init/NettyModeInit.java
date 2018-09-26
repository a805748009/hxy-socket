package com.mode.init;

import com.hxy.nettygo.result.NettyGoConstant;
import com.hxy.nettygo.result.Server;
import com.hxy.nettygo.result.base.config.ConfigForSSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 黄新宇
 * @Date 2018/6/8 下午2:59
 * @Description TODO
 **/
@Component
public class NettyModeInit {
    @Value("${netty.executorPoolMinSize}")
    private int executorPoolMinSize;
    @Value("${netty.executorPoolMaxSize}")
    private int executorPoolMaxSize;
    @Value("${netty.protoBuffType}")
    private String protoBuffType;
    @Value("${netty.connectType}")
    private String connectType;
    @Value("${netty.openSSL}")
    private boolean openSSL;
    @Value("${netty.certFilePath}")
    private String certFilePath;
    @Value("${netty.keyFilePath}")
    private String keyFilePath;
    @Value("${netty.port}")
    private int port;
    @Value("${netty.http_maxSize}")
    private int httpMaxSize;
    @Value("${zlib.compress.in}")
    private boolean zlibCompressIn;
    @Value("${zlib.compress.out}")
    private boolean zlibCompressOut;
    @Value("${zlib.compress.out.length}")
    private int zlibCompressOutLength;
    @Value("${crc32.compress.in}")
    private boolean crcCompressIn;
    @Value("${crc32.compress.out}")
    private boolean crcCompressOut;



    // 配置系统相关参数
    public void configurtaion() {
        // 1.设置nettygo模型参数
        NettyGoConstant.setNettyMode(executorPoolMinSize,executorPoolMaxSize, "BYTE");
        // 2.设置系统变量
        NettyGoConstant.setSystemMode(connectType);
        ConfigForSSL.ISOPENSSL = openSSL;
        if(ConfigForSSL.ISOPENSSL){
            ConfigForSSL.CERTFILEPATH = certFilePath;
            ConfigForSSL.KEYFILEPATH = keyFilePath;
        }
        // 3.开启登录验证时不需要登录就能访问的URL
        List<String> list = new ArrayList<String>();
        list.add("/login/thirdPartyLogin");
        list.add("/login/getSession");
        list.add("/notice/getnotice");
        list.add("/login/getOpenId");
        // 4.配置安全相关配置
        NettyGoConstant.setSecurityMode("ALLVALIDATE", list, 18000,true);
        // 5.配置zlib压缩相关
        NettyGoConstant.setZlibConfig(zlibCompressIn,zlibCompressOut,zlibCompressOutLength);
        // 6.配置crc32
        NettyGoConstant.setCrc32Config(crcCompressIn,crcCompressOut);
    }

    public void runNetty(){
        Server.run(port, httpMaxSize);
    }
}
