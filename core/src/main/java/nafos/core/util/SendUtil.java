package nafos.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月15日 下午8:39:09
 * 类说明
 */
public class SendUtil {
    private static final Logger logger = LoggerFactory.getLogger(SendUtil.class);


    public static Object castSendMsg(Object id, Object object) {
        //intBefore模式
        if (id instanceof byte[]) {
            if (object instanceof byte[])
                return ArrayUtil.concat((byte[]) id, (byte[]) object);
            return ArrayUtil.concat((byte[]) id, ProtoUtil.serializeToByte(object));
        }
        logger.error("================>>>>>>传入参数错误");
        return null;
    }
}
