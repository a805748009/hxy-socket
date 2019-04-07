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
    @Value("${nafos.security.sessionTimeOut:1800}")
    private int sessionTimeOut;


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
    private String ip;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.port1}")
    private int port1;
    @Value("${redis.password}")
    private String password;

    @Value("${nafos.security.limitOnType:NO}") //NO 不开启限流  LOCAL 本地单机限流   REDIS redis集群限流
    private String limitOnType;

    @Value("${nafos.security.iplimitTimeout:2}")
    private int iplimitTimeout;

    @Value("${nafos.security.iplimitCount:0}")
    private int iplimitCount;

    @Value("${nafos.security.alllimitTimeout:2}")
    private int alllimitTimeout;

    @Value("${nafos.security.alllimitCount:0}")
    private int alllimitCount;


    public boolean getIsUseRedis() {
        return isUseRedis;
    }

    public String getLimitOnType() {
        return limitOnType;
    }


    public int getSessionTimeOut() {
        return sessionTimeOut;
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

    public int getIplimitTimeout() {
        return iplimitTimeout;
    }

    public void setIplimitTimeout(int iplimitTimeout) {
        this.iplimitTimeout = iplimitTimeout;
    }

    public int getIplimitCount() {
        return iplimitCount;
    }

    public void setIplimitCount(int iplimitCount) {
        this.iplimitCount = iplimitCount;
    }

    public int getAlllimitTimeout() {
        return alllimitTimeout;
    }

    public void setAlllimitTimeout(int alllimitTimeout) {
        this.alllimitTimeout = alllimitTimeout;
    }

    public int getAlllimitCount() {
        return alllimitCount;
    }

    public void setAlllimitCount(int alllimitCount) {
        this.alllimitCount = alllimitCount;
    }
}
