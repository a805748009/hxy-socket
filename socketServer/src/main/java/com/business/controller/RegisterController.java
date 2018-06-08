package com.business.controller;

import com.result.base.annotation.On;
import com.result.base.annotation.Route;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年3月9日 下午3:07:10 注册 用户连接时操作
 */
@Route
public class RegisterController {
	private static final Logger logger = LoggerFactory.getLogger(ExitController.class);

	@On("Connect")
	public void onConnect(Channel channel) {
		logger.info("有人加入");
	}


}
