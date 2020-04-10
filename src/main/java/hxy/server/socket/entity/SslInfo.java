package hxy.server.socket.entity;

/**
 * @ClassName SslInfo
 * @Author hxy
 * @Date 2020/4/8 18:38
 */
public class SslInfo {
    private boolean open = false;
    private String certFilePath = null;
    private String keyFilePath = null;

    public SslInfo() {
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }
}
