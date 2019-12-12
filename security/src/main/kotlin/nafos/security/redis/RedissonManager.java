package nafos.security.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName (RedisManager)
 * @Desc redisson
 * @Author hxy
 * @Date 2019/7/1 15:52
 **/
public class RedissonManager {
    private static final Logger logger = LoggerFactory.getLogger(RedissonManager.class);

    private static RedissonClient redisson = null;


    public static void init(){
        init("redisson.yaml");
    }

    public static void init(String filePath){
        logger.info("===>>>初始化redisson");
        Config config = null;
        try {
            config = Config.fromYAML(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        init(config);
    }

    public static void init(Config config){
        redisson = Redisson.create(config);
    }

    public static void initForJson(String filePath){
        logger.info("===>>>初始化redisson");
        Config config = null;
        try {
            config =  Config.fromJSON(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        init(config);
    }


    public static RedissonClient getRedisson(){
        if(redisson == null){
            synchronized (RedissonManager.class){
                if(redisson == null){
                    init();
                }
            }
        }
        return redisson;
    }
}
