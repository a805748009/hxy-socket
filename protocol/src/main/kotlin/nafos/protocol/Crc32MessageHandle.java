package nafos.protocol;


import nafos.server.NafosServer;
import nafos.server.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.zip.CRC32;

/**
 * @Author 黄新宇
 * @Date 2018/7/4 下午2:00
 * @Description TODO
 **/
public class Crc32MessageHandle {
    private static final Logger logger = LoggerFactory.getLogger(Crc32MessageHandle.class);

    private static Crc32MessageHandle crc32MessageHandle = new Crc32MessageHandle();

    public static Crc32MessageHandle getInstance(){
        return crc32MessageHandle;
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午2:34
     * @Description(获取crc32的long码)
     */
    public long getCrc32Long(byte[] bye) {
        CRC32 crc32 = new CRC32();
        crc32.update(bye);
        return crc32.getValue();
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午2:34
     * @Description(获取crc32的int码)
     */
    public int getCrc32Int(byte[] bye) {
        return Crc32Util.calculate(bye);
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午2:36
     * @Description(前面加上校验字节)
     */
    public byte[] addCrc32IntBefore(byte[] bye) {
        if (!((ProtocolSocketConfiguration) NafosServer.configuration).isCrc32Out()) {
            return bye;
        }
        return ArrayUtil.concat(ArrayUtil.intToByteArray(getCrc32Int(bye)), bye);
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午2:36
     * @Description(前面加上校验字节)
     */
    public byte[] checkCrc32IntBefore(byte[] bye) {
        if (!((ProtocolSocketConfiguration) NafosServer.configuration).isCrc32In()) {
            return bye;
        }
        byte[] crc32byte = new byte[4];
        System.arraycopy(bye, 0, crc32byte, 0, 4);
        byte[] content = new byte[bye.length - 4];
        System.arraycopy(bye, 4, content, 0, bye.length - 4);
        //校验失败，数据被修改
        if (getCrc32Int(content) != ArrayUtil.byteArrayToInt(crc32byte)) {
            logger.error("==============>>>>>>>CRC32校验失败");
            return null;
        }
        return content;
    }


}
