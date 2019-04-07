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


    @Value("${redis.pool.maxActive:0}")
    private int maxActive;
    @Value("${redis.pool.maxIdle:0}")
    private int maxIdle;
    @Value("${redis.pool.maxWait:0}")
    private int maxWait;
    @Value("${redis.pool.testOnBorrow:false}")
    private boolean testOnBorrow;
    @Value("${redis.pool.testOnReturn:false}")
    private boolean testOnReturn;
    @Value("${redis.ip:}")
    private String ip;
    @Value("${redis.port:0}")
    private int port;
    @Value("${redis.port1:0}")
    private int port1;
    @Value("${redis.password:}")
    private String password;




    public boolean getIsUseRedis() {
        return isUseRedis;
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



    public void setUseRedis(boolean useRedis) {
        isUseRedis = useRedis;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPort1(int port1) {
        this.port1 = port1;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
