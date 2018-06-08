package com.business.controller;

import com.business.entry.SocketMessage;
import com.business.entry.User;
import com.result.base.annotation.On;
import com.result.base.annotation.Route;
import com.result.base.cache.Client;
import com.result.base.tools.CastUtil;
import com.result.base.tools.SerializationUtil;
import com.result.base.tools.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月15日 下午8:35:11 初始功能，创建房间，加入房间
 */
@Route
public class RoomController {
	private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

	/**
	* @Author 黄新宇
	* @date 2018/5/2 下午6:16
	*
	* @param [client, socketMsg]
	* @return void
	*/

	@On("createRoom")
	public void createRoom(Client client, SocketMessage socketMsg) {
		client.joinRoom(CastUtil.castString(SnowflakeIdWorker.getSnowflakeIdWorker().nextId()));
	}


	@On("leaveRoom")
	public void leaveRoom(Client client, SocketMessage socketMsg) {
	    client.roomBroadcast(SerializationUtil.serializeToByte(new User()));
	    client.leaveRoom();
	}

	/**
	 * 
	 * @author huangxinyu @version 创建时间：2018年3月9日 下午7:52:31 @Description:
	 *         加入房间 @param @param channel @param @param socketMsg @param @param
	 *         frame 设定文件 @return void 返回类型 @throws
	 */
	@On("joinRoom")
	public void joinRoom(Client client, SocketMessage socketMsg) {
		SocketMessage repBuild = new SocketMessage();
		repBuild.setClientUri("ErrorMsg");
		repBuild.setParam1("房间已经解散...");
		byte[] by = SerializationUtil.serializeToByte(repBuild);
		client.sendMsg(by);
	}

}
