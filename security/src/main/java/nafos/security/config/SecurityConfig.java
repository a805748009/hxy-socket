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

    // ALLVALIDATE 全部需要验证
    // NOVALIDATE 全部不需要验证
    @Value("${nafos.security.isValidate:NOVALIDATE}")
    private String isValidate;

    //安全模式相反的HttpUurlList  逗号隔开
    @Value("${nafos.security.oppositeHttpList}")
    private String oppositeHttpList;

    //安全模式相反的code  逗号隔开
    @Value("${nafos.security.oppositeCodeList}")
    private String oppositeCodeList;

    //session过期时间，秒为单位
    @Value("${nafos.security.sessionTimeOut}")
    private int sessionTimeOut;


    public boolean getIsUseRedis(){
        return isUseRedis;
    }

    public String getIsValidate(){
        return isValidate;
    }

    public String getOppositeHttpList(){
        return oppositeHttpList;
    }

    public String getOppositeCodeList(){
        return oppositeCodeList;
    }


    public int getSessionTimeOut(){
        return sessionTimeOut;
    }
}
