package nafos.core.nafosEnum;

/**
 * 连接形式
 */
public enum ConnectEnum {
    HTTP("HTTP"),

    SOCKET("SOCKET");

    private String type;

    private ConnectEnum(String type) {
        this.type = type;
    }

    private ConnectEnum() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
