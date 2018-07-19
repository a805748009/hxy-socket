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
        list.add("/backstage/login");
        list.add("/backstage/getSession");
        list.add("/SystemInfo/getNowUserCount");
        list.add("/SystemInfo/getSevenActiveCount");
        list.add("/SystemInfo/getSevenLoginTimeInfo");
        list.add("/SystemInfo/getOnlineCountInfo");
        list.add("/SystemInfo/getLoginTimenInterval");
        list.add("/SystemInfo/getJVMInfo");
        list.add("/SystemInfo/updateShareOpen");
        list.add("/SystemInfo/getShareOpen");
        list.add("/SystemInfo/getDruidInfoListPage");
        list.add("/SystemInfo/deleteDruidInfoById");
        list.add("/Advertising/getAllAdvertising");
        list.add("/Advertising/updateAdvertisingById");
        list.add("/Advertising/deleteAdvertisingById");
        list.add("/Advertising/addAdvertising");
        list.add("/hotBar/getBanners");
        list.add("/hotBar/updateBanner");
        list.add("/hotBar/deleteBanner");
        list.add("/hotBar/addBanner");
        list.add("/hotBar/getGames");
        list.add("/hotBar/updateGame");
        list.add("/hotBar/deleteGame");
        list.add("/hotBar/addGame");
        list.add("/hotBar/getAllTypes");
        list.add("/hotBar/getGameByType");

        // 4.配置安全相关配置
        NettyGoConstant.setSecurityMode("ALLVALIDATE", list, 18000,false);
    }

    public void runNetty(){
        Server.run(port, httpMaxSize);
    }

}
