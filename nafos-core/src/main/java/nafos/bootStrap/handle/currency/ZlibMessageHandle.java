package nafos.bootStrap.handle.currency;

import nafos.core.util.ArrayUtil;
import nafos.core.util.ZlibUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @Author 黄新宇
 * @Date 2018/6/25 上午10:01
 * @Description TODO
 **/
@Service
public class ZlibMessageHandle {
    private static final Logger logger = LoggerFactory.getLogger(ZlibMessageHandle.class);

    @Value("${nafos.isZlibIn:false}")
    public boolean isZlibIn;
    @Value("${nafos.isZlibOut:false}")
    public boolean isZlibOut;
    @Value("${nafos.zlibOutMinLength:50}")
    public int zlibOutMinLength;

    /**
     * @param
     * @return byte[]
     * @Author 黄新宇
     * @date 2018/6/25 上午10:54
     * @Description(解压byte数据)
     */
    public byte[] unZlibByteMessage(byte[] bytes) {
        if (!isZlibIn) {
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

    /**
     * 压缩
     *
     * @param bytes
     * @return
     */
    public byte[] zlibByteMessage(byte[] bytes) {
        if (!isZlibOut) {
            return bytes;
        }
        if (bytes.length > zlibOutMinLength) {
            return ArrayUtil.concat(ArrayUtil.intToByteArray(1), ZlibUtil.compress(bytes));
        }
        return ArrayUtil.concat(ArrayUtil.intToByteArray(0), bytes);
    }


}
