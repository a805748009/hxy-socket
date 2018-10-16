package nafos.security.scheduled;

import nafos.security.cache.CacheMapDao;
import nafos.security.config.SecurityConfig;
import nafos.security.manager.AutoCloseChannelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @Author 黄新宇
 * @Date 2018/10/15 下午5:12
 * @Description TODO
 **/
@Component
public class TimeTask {
    private static final Logger logger = LoggerFactory.getLogger(TimeTask.class);
    @Autowired
    SecurityConfig securityConfig;


    @Scheduled(cron="0 0 4 * * ?")
    public void executeFileDownLoadTask() {
        CacheMapDao.delTimeOut();
        logger.info("过时的session清除完毕============");
    }

    @Scheduled(cron="0 0/1 * * * ?")
    public void closeUnSafeChannel() {
        AutoCloseChannelManager.closeUnSafeChannel(securityConfig.getChannelUnSafeConnectTime());
    }
}
