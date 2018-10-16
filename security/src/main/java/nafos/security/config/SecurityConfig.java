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
    @Value("${nafos.security.oppositeHttpList:}")
    private String oppositeHttpList;

    //安全模式相反的code  逗号隔开
    @Value("${nafos.security.oppositeCodeList:}")
    private String oppositeCodeList;

    //session过期时间，秒为单位
    @Value("${nafos.security.sessionTimeOut:1800}")
    private int sessionTimeOut;

    //不通过安全验证连接允许存活的时间
    @Value("${nafos.security.channelUnSafeConnectTime:300000}")
    private long channelUnSafeConnectTime;


    @Value("${redis.pool.maxActive}")
    private int maxActive;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.pool.maxWait}")
    private int maxWait;
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.pool.testOnReturn}")
    private boolean testOnReturn;
    @Value("${redis.ip}")
    private String  ip;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.port1}")
    private int port1;
    @Value("${redis.password}")
    private String password;


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

    public long getChannelUnSafeConnectTime(){
        return channelUnSafeConnectTime;
    }


    public int getMaxActive() {
        return maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getPort1() {
        return port1;
    }

    public String getPassword() {
        return password;
    }
}
