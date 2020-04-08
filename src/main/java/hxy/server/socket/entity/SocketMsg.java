package hxy.server.socket.entity;

/**
 * @Description socket消息体
 * @Author xinyu.huang
 * @Time 2020/4/8 23:24
 */
public class SocketMsg {
    int code;
    String text;

    public SocketMsg(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
