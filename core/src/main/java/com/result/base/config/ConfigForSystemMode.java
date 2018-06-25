package com.result.base.config;

import com.result.base.enums.ConnectType;
import com.result.base.enums.FlushMessageTransformation;
import com.result.base.enums.SocketBinaryType;

import java.util.HashMap;
import java.util.Map;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月28日 下午6:42:10 
* 类说明 
*/
public class ConfigForSystemMode {
	
		//通讯方式
		public static String CONNECTTYPE = ConnectType.HTTP.getType();
		
		//socket二进制编码方式
		public static String BINARYTYPE = SocketBinaryType.INTBEFORE.getType();

		//socket ID路由map
		public static Map<Integer,String> SOCKETROUTEMAP =  new HashMap<>();

		public static String FLUSHMESSAGETRANSFORMATION = FlushMessageTransformation.BYTE.getType();

		//远程调用开头第一节uri
		public static String REMOTE_CALL_URI = "/bcRemoteCall";

		public static boolean IS_ZLIB_COMPRESS_IN = false;

		public static boolean IS_ZLIB_COMPRESS_OUT = false;

		public static int IS_ZLIB_COMPRESS_OUT_LENGTH = 50;

}
