package com.result.base.handle;

import com.result.base.tools.ArrayUtil;
import com.result.base.tools.CastUtil;
import java.util.zip.CRC32;

/**
 * @Author 黄新宇
 * @Date 2018/7/4 下午2:00
 * @Description TODO
 **/
public class Crc32MessageHandle {


    /**
    * @Author 黄新宇
    * @date 2018/7/4 下午2:34
    * @Description(获取crc32的int码)
    * @param
    * @return int
    */
    public static int getCrc32Int(byte[] bye){
        CRC32 crc32 = new CRC32();
        crc32.update(bye);
        return CastUtil.castInt(crc32.getValue());
    }

    /**
    * @Author 黄新宇
    * @date 2018/7/4 下午2:36
    * @Description(前面加上校验字节)
    * @param
    * @return byte[]
    */
    public static byte[] addCrc32IntBefore(byte[] bye){
        return ArrayUtil.concat(ArrayUtil.intToByteArray(getCrc32Int(bye)),bye);
    }

    /**
     * @Author 黄新宇
     * @date 2018/7/4 下午2:36
     * @Description(前面加上校验字节)
     * @param
     * @return byte[]
     */
    public static byte[] checkCrc32IntBefore(byte[] bye){
        byte[] crc32byte = new byte[4];
        System.arraycopy(bye, 0, crc32byte, 0, 4);
        byte[] content = new byte[bye.length-4];
        System.arraycopy(bye, 4, content, 0, bye.length-4);
        //校验失败，数据被修改
        if(getCrc32Int(content)!=ArrayUtil.byteArrayToInt(crc32byte)){
            return null;
        }
        return content;
    }



}
