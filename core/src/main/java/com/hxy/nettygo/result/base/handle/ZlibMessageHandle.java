package com.hxy.nettygo.result.base.handle;

import com.hxy.nettygo.result.base.config.ConfigForSystemMode;
import com.hxy.nettygo.result.base.tools.ArrayUtil;
import com.hxy.nettygo.result.base.tools.ZlibUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author 黄新宇
 * @Date 2018/6/25 上午10:01
 * @Description TODO
 **/
public class ZlibMessageHandle   {
    private static final Logger logger = LoggerFactory.getLogger(ZlibMessageHandle.class);


    /**
    * @Author 黄新宇
    * @date 2018/6/25 上午10:54
    * @Description(解压byte数据)
    * @param
    * @return byte[]
    */
    public static byte[] unZlibByteMessage( byte[] bytes){
        if(!ConfigForSystemMode.IS_ZLIB_COMPRESS_IN)
            return bytes;
        byte[] bs = new byte[4];
        System.arraycopy(bytes, 0, bs, 0, 4);
        byte[] content = new byte[bytes.length-4];
        System.arraycopy(bytes, 4, content, 4, bytes.length-4);
        if(ArrayUtil.byteArrayToInt(bs)==1){
            logger.debug("解压数据中-------》》》》》");
            content = ZlibUtil.decompress(content);
        }
        return content;
    }

    /**
     * 压缩
     * @param bytes
     * @return
     */
    public static byte[] zlibByteMessage( byte[] bytes){
        if(!ConfigForSystemMode.IS_ZLIB_COMPRESS_OUT)
            return bytes;
       if(bytes.length>ConfigForSystemMode.IS_ZLIB_COMPRESS_OUT_LENGTH){
           return ArrayUtil.concat(ArrayUtil.intToByteArray(1),ZlibUtil.compress(bytes));
       }
        return ArrayUtil.concat(ArrayUtil.intToByteArray(0),bytes);
    }



}
