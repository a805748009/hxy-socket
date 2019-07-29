package nafos.security.redis;

/**
 * @ClassName (SecurityUpdate)
 * @Desc DOTO
 * @Author hxy
 * @Date 2019/7/1 21:11
 **/
public class SecurityUpdate {
    private String action;
    private String sessionId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SecurityUpdate() {
    }

    public SecurityUpdate(String action, String sessionId) {
        this.action = action;
        this.sessionId = sessionId;
    }
}
