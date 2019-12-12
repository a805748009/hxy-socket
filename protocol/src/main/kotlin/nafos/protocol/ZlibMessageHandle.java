package nafos.protocol;

import nafos.server.NafosServer;
import nafos.server.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author 黄新宇
 * @Date 2018/6/25 上午10:01
 **/
public class ZlibMessageHandle {
    private static final Logger logger = LoggerFactory.getLogger(ZlibMessageHandle.class);

    private ProtocolSocketConfiguration socketConfiguration = (ProtocolSocketConfiguration) NafosServer.configuration;
    
    private static ZlibMessageHandle zlibMessageHandle = new ZlibMessageHandle();
    
    public static ZlibMessageHandle getInstance(){
        return zlibMessageHandle;
    }

    /**
     * @Author 黄新宇
     * @date 2018/6/25 上午10:54
     * @Description(解压byte数据)
     */
    public byte[] unZlibByteMessage(byte[] bytes) {
        if (!socketConfiguration.isZlibIn()) {
            return bytes;
        }
        byte[] bs = new byte[4];
        System.arraycopy(bytes, 0, bs, 0, 4);
        byte[] content = new byte[bytes.length - 4];
        System.arraycopy(bytes, 4, content, 0, bytes.length - 4);
        if (ArrayUtil.byteArrayToInt(bs) == 1) {
            logger.debug("解压数据中-------》》》》》");
            content = ZlibUtil.decompress(content);
        }
        return content;
    }

    /***
     *@Description 压缩
     *@Author      xinyu.huang
     *@Time        2019/11/24 22:15
     */
    public byte[] zlibByteMessage(byte[] bytes) {
        if (!socketConfiguration.isZlibOut()) {
            return bytes;
        }
        if (bytes.length > socketConfiguration.getZlibOutMinLength()) {
            return ArrayUtil.concat(ArrayUtil.intToByteArray(1), ZlibUtil.compress(bytes));
        }
        return ArrayUtil.concat(ArrayUtil.intToByteArray(0), bytes);
    }


}
