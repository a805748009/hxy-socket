package com.mode.init;

import com.hxy.nettygo.result.NettyGoConstant;
import com.hxy.nettygo.result.Server;
import com.hxy.nettygo.result.base.config.ConfigForSSL;
import com.hxy.nettygo.result.base.enums.SocketBinaryType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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


    // 配置系统相关参数
    public void configurtaion() {
        //2. 设置系统变量
        NettyGoConstant.setSystemMode(connectType);
        //3. 设置是否开启SSL  部署到服务器才开启
        ConfigForSSL.ISOPENSSL = openSSL;
        if(ConfigForSSL.ISOPENSSL){
            ConfigForSSL.CERTFILEPATH = certFilePath;
            ConfigForSSL.KEYFILEPATH = keyFilePath;
        }
        // 4.设置nettygo模型参数
        NettyGoConstant.setNettyMode(executorPoolMinSize,executorPoolMaxSize, "BYTE");
        NettyGoConstant.setRedisSecurityMode(18000, true);
        NettyGoConstant.setBinaryType(SocketBinaryType.PARENTFORBASESOCKETMESSAGE.getType());
        new RouteMapInit();//byteId对应的路由。INTBEFORE模式下需要加载

        // 5.配置zlib压缩相关
        NettyGoConstant.setZlibConfig(zlibCompressIn,zlibCompressOut,zlibCompressOutLength);
    }

    public void runNetty(){
        Server.run(port, httpMaxSize);
    }
}
