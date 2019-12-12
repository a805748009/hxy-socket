package nafos.security.config;


/**
 * @Author 黄新宇
 * @Date 2018/10/13 下午1:59
 * @Description 配置
 **/
public class SecurityConfig {
    /**
     * 是否开启redis做单点登录
     */
    private static boolean useRedis = false;

    /**
     * session过期时间，秒为单位 默认3天
     */
    private static int sessionTimeOut = 259200;

    public static void init(boolean useRedisP,int sessionTimeOutP){
        useRedis = useRedisP;
        sessionTimeOut = sessionTimeOutP;
    }

    public static boolean isUseRedis() {
        return useRedis;
    }

    public static int getSessionTimeOut() {
        return sessionTimeOut;
    }
}
