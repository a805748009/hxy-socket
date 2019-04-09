package nafos.security;

import nafos.NafosServer;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.security.currentLimiting.LimitEnum;
import nafos.security.currentLimiting.SecurityHttpLimitingHandle;
import org.springframework.stereotype.Component;

@Component
public class SecurityNafosServer extends NafosServer {

    public SecurityNafosServer registLimiting(LimitEnum type, int timeOut, int limitCount){
        SecurityHttpLimitingHandle slh = SpringApplicationContextHolder.getSpringBeanForClass(SecurityHttpLimitingHandle.class);
        if (type.equals(LimitEnum.LOCALAll)) {
            slh.limitOnType = "LOCAL";
            slh.alllimitTimeout = timeOut;
            slh.alllimitCount = limitCount;
            SpringApplicationContextHolder.getSpringBeanForClass(ScurityRunner.class).registLimiting();
        }
        if (type.equals(LimitEnum.LOCALIP)) {
            slh.limitOnType = "LOCAL";
            slh.iplimitTimeout = timeOut;
            slh.iplimitCount = limitCount;
            SpringApplicationContextHolder.getSpringBeanForClass(ScurityRunner.class).registLimiting();
        }
        if (type.equals(LimitEnum.REDISALL)) {
            slh.limitOnType = "REDIS";
            slh.alllimitTimeout = timeOut;
            slh.alllimitCount = limitCount;
            SpringApplicationContextHolder.getSpringBeanForClass(ScurityRunner.class).registLimiting();
        }
        if (type.equals(LimitEnum.REDISIP)) {
            slh.limitOnType = "REDIS";
            slh.iplimitTimeout = timeOut;
            slh.iplimitCount = limitCount;
            SpringApplicationContextHolder.getSpringBeanForClass(ScurityRunner.class).registLimiting();
        }
        return this;
    }
}
