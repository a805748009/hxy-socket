package com.business.controller;

import com.business.entry.SocketMessage;
import com.business.entry.User;
import com.hxy.nettygo.result.base.annotation.On;
import com.hxy.nettygo.result.base.annotation.Route;
import com.hxy.nettygo.result.base.cache.Client;
import com.hxy.nettygo.result.base.security.SecurityUtil;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.hxy.nettygo.result.base.tools.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月16日 下午3:58:40 类说明
 */

@Route
public class ExitController {
	private static final Logger logger = LoggerFactory.getLogger(ExitController.class);

	@On("Disconnect")
	public void onDisconnect(Client client) {
		if (ObjectUtil.isNull(client))
			return;
		logger.info("有人退出了");
		client.leaveNameSpace();
		if (!client.isJoinRoom())
			return;

	}
}
