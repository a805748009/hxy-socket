package nafos.msgQueue.rabbitMq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/11/7 下午3:13
 * @Description TODO
 **/
@Component
public class MqConfig {

    @Value("${nafos.msgQue.mq.mqHost:}")
    private String mqHost;

    @Value("${nafos.msgQue.mq.mqUserName:}")
    private String mqUserName;

    @Value("${nafos.msgQue.mq.mqPassWord:}")
    private String mqPassWord;

    public String getMqHost() {
        return mqHost;
    }


    public String getMqUserName() {
        return mqUserName;
    }


    public String getMqPassWord() {
        return mqPassWord;
    }

    public void setMqHost(String mqHost) {
        this.mqHost = mqHost;
    }

    public void setMqUserName(String mqUserName) {
        this.mqUserName = mqUserName;
    }

    public void setMqPassWord(String mqPassWord) {
        this.mqPassWord = mqPassWord;
    }
}
