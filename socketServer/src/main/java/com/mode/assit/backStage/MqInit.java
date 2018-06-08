package com.mode.assit.backStage;

import com.result.NettyGoConstant;
import com.result.base.tools.CastUtil;
import com.result.base.tools.ConfigUtil;

import java.util.ResourceBundle;

/**
 * @Author 黄新宇
 * @Date 2018/5/11 下午4:13
 * @Description TODO
 **/
public class MqInit {

    public static void initMQ(){
        ResourceBundle bundle = ConfigUtil.getBundle("/socketConf/rinzz.properties");
        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[rinzz.properties] is not found!");
        }
        boolean mqOpen = CastUtil.castBoolean(bundle.getString("mqOpen"));
        String mqHost = CastUtil.castString(bundle.getString("mqHost"));
        String mqUserName = CastUtil.castString(bundle.getString("mqUserName"));
        String mqPassWord = CastUtil.castString(bundle.getString("mqPassWord"));
        NettyGoConstant.setMqMode(mqOpen,mqHost,mqUserName,mqPassWord);
    }
}
