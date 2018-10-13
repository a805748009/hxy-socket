package nafos.network.bootStrap.netty.handle.currency;


import nafos.core.util.ArrayUtil;
import nafos.core.util.Crc32Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.zip.CRC32;

/**
 * @Author 黄新宇
 * @Date 2018/7/4 下午2:00
 * @Description TODO
 **/
@Component
public class Crc32MessageHandle {
    private static final Logger logger = LoggerFactory.getLogger(Crc32MessageHandle.class);

    @Value("${nafos.isCrc32Out:false}")
    public  boolean isCrc32Out ;
    @Value("${nafos.isCrc32In:false}")
    public boolean isCrc32In ;

    /**
    * @Author 黄新宇
    * @date 2018/7/4 下午2:34
    * @Description(获取crc32的long码)
    * @param
    * @return int
    */
    public  long getCrc32Long(byte[] bye){
        CRC32 crc32 = new CRC32();
        crc32.update(bye);
        long ai = crc32.getValue();
        return crc32.getValue();
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午2:34
     * @Description(获取crc32的int码)
     * @param
     * @return int
     */
    public  int getCrc32Int(byte[] bye){
        return Crc32Util.calculate(bye);
    }

    /**
    * @Author 黄新宇
    * @date 2018/7/4 下午2:36
    * @Description(前面加上校验字节)
    * @param
    * @return byte[]
    */
    public  byte[] addCrc32IntBefore(byte[] bye){
        if(!isCrc32Out)
            return bye;
        return ArrayUtil.concat(ArrayUtil.intToByteArray(getCrc32Int(bye)),bye);
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午2:36
     * @Description(前面加上校验字节)
     * @param
     * @return byte[]
     */
    public  byte[] checkCrc32IntBefore(byte[] bye){
        if(!isCrc32In)
            return bye;
        byte[] crc32byte = new byte[4];
        System.arraycopy(bye, 0, crc32byte, 0, 4);
        byte[] content = new byte[bye.length-4];
        System.arraycopy(bye, 4, content, 0, bye.length-4);
        //校验失败，数据被修改
        if(getCrc32Int(content)!=ArrayUtil.byteArrayToInt(crc32byte)){
            logger.error("==============>>>>>>>CRC32校验失败");
            return null;
        }
        return content;
    }



}
