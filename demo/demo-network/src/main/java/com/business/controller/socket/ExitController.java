package com.business.controller.socket;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import nafos.core.annotation.controller.DisConnect;
import nafos.core.annotation.controller.SocketActive;
import nafos.core.util.ObjectUtil;
import nafos.game.relation.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月16日 下午3:58:40 类说明
 */

@SocketActive
public class ExitController {
	private static final Logger logger = LoggerFactory.getLogger(ExitController.class);

	@DisConnect
	public void onDisconnect(Channel channel) {
		Object client =  channel.attr(AttributeKey.valueOf("client")).get();
		if(ObjectUtil.isNotNull(client))
			((Client) client).leaveNameSpace();
		logger.info("有人退出了");
	}
}
