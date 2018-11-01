package com.mode.filter;

import com.business.entry.bean.User;
import nafos.security.SecurityUtil;
import nafos.security.filter.SessionTimeUpdate;

/**
 * @Author 黄新宇
 * @Date 2018/5/17 下午3:35
 * @Description TODO
 **/
public class SessionTimeUpdateDo implements SessionTimeUpdate {
    @Override
    public void run(String sessionId) {
        String userId = SecurityUtil.getLoginUser(sessionId, User.class).getUserId();
        //重新加载所有的用户属性到redis
//        SpringApplicationContextHolder.getSpringBeanForClass(RedisLoadService.class).updateTimeByUserId(userId);
    }

}
