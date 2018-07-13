package com.business.controller;

import java.util.Map;

import com.business.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.business.bean.BaseMessage;
import com.business.service.LoginService;
import com.hxy.nettygo.result.base.annotation.Nuri;
import com.hxy.nettygo.result.base.annotation.Route;
import com.hxy.nettygo.result.base.entry.Result;
import com.hxy.nettygo.result.base.pool.ThreadLocalUtil;
import com.hxy.nettygo.result.base.security.SecurityUtil;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.hxy.nettygo.result.base.tools.SerializationUtil;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月9日 下午3:32:21 登录接口
 */
@Route
@Nuri(uri = "/login")
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	LoginService loginService;


	@Nuri(uri = "/thirdPartyLogin", method = "POST")
	public Object thirdPartyLogin(User user) {
		BaseMessage basemsg = new BaseMessage();
		basemsg.setId(loginService.regiest(user));
		return SerializationUtil.serializeToByte(basemsg);
	}

	@Nuri(uri = "/getSession", method = "GET", type = "JSON")
	public Object getSession(Map<String, String> map) {
		String sessionId = ThreadLocalUtil.getRequest().getSecurityCookieId();
		System.out.println("session=" + sessionId);
		// 0.设置登录cookie
		if (ObjectUtil.isNull(sessionId))
			sessionId = ThreadLocalUtil.getRespone().setGoSession();
		System.out.println("session=" + sessionId);
		return sessionId;
	}
	
	@Nuri(uri = "/345", method = "POST", type = "JSON")
	public Object tes23(Map<String, String> map) throws NoSuchMethodException, SecurityException {
		System.out.println("222222222r22");
		User user = new User("112222", "aini", true, "12kpojoda", "15608419462", "sda", "doajsodj", "");
		return user;
	}

	@Nuri(uri = "/logout", method = "GET", type = "JSON")
	public Object logout(Map<String, String> map) {
		String sessionId = ThreadLocalUtil.getRequest().getSecurityCookieId();
		SecurityUtil.logout(sessionId);
		return new Result(true);
	}


}
