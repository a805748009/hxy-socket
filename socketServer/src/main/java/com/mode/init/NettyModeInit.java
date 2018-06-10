package com.mode.init;

import com.result.NettyGoConstant;
import com.result.Server;
import com.result.base.config.ConfigForSSL;
import com.result.base.enums.SocketBinaryType;
import com.result.base.tools.CastUtil;
import com.result.base.tools.ConfigUtil;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
        NettyGoConstant.setRedisSecurityMode(18000, false);
        NettyGoConstant.setBinaryType(SocketBinaryType.PARENTFORBASESOCKETMESSAGE.getType());
        new RouteMapInit();//byteId对应的路由。INTBEFORE模式下需要加载
        // 5.启动netty
        Server.run(port, httpMaxSize);
    }
}
