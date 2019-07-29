package nafos.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/10/13 下午1:59
 * @Description TODO
 **/
@Component
public class SecurityConfig {
    @Value("${nafos.security.isUseRedis:false}")
    private boolean isUseRedis;

    //session过期时间，秒为单位
    @Value("${nafos.security.sessionTimeout:1800}")
    private int sessionTimeOut;


    public boolean getIsUseRedis() {
        return isUseRedis;
    }


    public int getSessionTimeOut() {
        return sessionTimeOut;
    }




}
