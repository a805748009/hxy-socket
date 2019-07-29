package nafos.core.entry.error;

/**
 * @Author 黄新宇
 * @Date 2018/10/27 下午3:35
 * @Description TODO
 **/
public class BizException extends RuntimeException {

    private Integer status = 400; //response.status 错误码
    private Integer error; //body 中返回的业务具体状态
    private String message;//具体错误信息

    public static BizException LOGIN_SESSION_TIME_OUT = new BizException(600, "登录失效，请重新登录");

    public BizException() {
    }

    public BizException(String errmsg) {
        super(errmsg);
        this.message = errmsg;
    }


    public BizException(int error, String errmsg, int status) {
        super(errmsg);
        this.error = error;
        this.message = errmsg;
        this.status = status;
    }

    public BizException(int error, String errmsg) {
        super(errmsg);
        this.error = error;
        this.message = errmsg;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"error\":" + error +
                ", \"message\":\"" + message + "\"" + "}";
    }
}

