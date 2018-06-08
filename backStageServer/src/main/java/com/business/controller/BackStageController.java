package com.business.controller;

import com.business.service.UserService;
import com.business.tools.ResponseTool;
import com.result.base.annotation.Nuri;
import com.result.base.annotation.Route;
import com.result.base.entry.backStageBean.Admin;
import com.result.base.pool.ThreadLocalUtil;
import com.result.base.security.SecurityUtil;
import com.result.base.tools.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月9日 下午3:32:21 登录接口
 */
@Route
@Nuri(uri = "/backstage")
public class BackStageController {
    private static final Logger logger = LoggerFactory.getLogger(BackStageController.class);

    @Autowired
    UserService userService;

    @Nuri(uri = "/login", method = "POST", type = "JSON")
    public Object getUserById(Map<String, Object> map) {
        String userId = map.get("userId").toString();
        String password = map.get("password").toString();
        Admin user = userService.selectUserById(userId);
        String sessionId = ThreadLocalUtil.getRequest().getSecurityCookieId();
        if (ObjectUtil.isNotNull(user)) {
            if (password.equals(user.getUserPass())) {
                SecurityUtil.setLoginUser(sessionId,user);
                return ResponseTool.newObjectResponse(user);
            }
        }
        return ResponseTool.newErrorResponse("账号或密码不正确");
    }

    @Nuri(uri = "/getSession",method = "GET",type = "JSON")
    public Object getSession(Map map){
        String sessionId = ThreadLocalUtil.getRequest().getSecurityCookieId();
        // 0.设置登录cookie
        if (ObjectUtil.isNull(sessionId))
            sessionId = ThreadLocalUtil.getRespone().setGoSession();
        Map<String,String> res = new HashMap<>();
        res.put("session",sessionId);
        return res;
    }
}
