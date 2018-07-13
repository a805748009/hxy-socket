package com.hxy.nettygo.result.base.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月6日 下午2:22:14 
* Kryo工具类
*/
public class KyroUtil {
	private static Logger logger = LoggerFactory.getLogger(KyroUtil.class);
	
	//private static KryoPool pool;
	//原本打算使用kyro序列化session，后来发现kyro对session序列化不支持，反序列后得不到value。   这种out序列化测试性能消耗时间更短，但是长度变大4倍意思，待优化 
//    static{
//    	KryoFactory factory = new KryoFactory() {
//            public Kryo create() {
//                Kryo kryo = new Kryo();
//                kryo.setReferences(false);
//                //把shiroSession的结构注册到Kryo注册器里面，提高序列化/反序列化效率
//                kryo.register(Session.class, new JavaSerializer());
//                kryo.register(String.class, new JavaSerializer());
//                kryo.register(User.class, new JavaSerializer());
//                kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
//                return kryo;
//            }
//    	};
//        pool = new KryoPool.Builder(factory).build();   
//        logger.info("KryoPool初始化成功====================================");
//    }
	
	/**
	 * 对象编码
	 * @param value
	 * @return
	 */
	 public static  String serialization(Object value) {
//		 String str ="";
//		 try {
//		 	Kryo kryo = pool.borrow();
//		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		    Output output = new Output(baos);
//		    kryo.writeClassAndObject(output, value);
//		    output.flush();
//		    output.close();
//		    byte[] b = baos.toByteArray();
//		      baos.flush();
//		      baos.close();
//		      str = new String(b, "ISO8859-1");
//		 	} catch (IOException e) {
//		      e.printStackTrace();
//		    }
//		  return str;
		 
//		 
		 ByteArrayOutputStream bos = null;  
	        ObjectOutputStream oos = null;  
	        try {  
	            bos = new ByteArrayOutputStream();  
	            oos = new ObjectOutputStream(bos);  
	            oos.writeObject(value);  
	            return new String(bos.toByteArray(), "ISO8859-1");  
	        } catch (Exception e) {  
	            throw new RuntimeException("serialize session error", e);  
	        } finally {  
	            try {  
	                oos.close();  
	                bos.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	  
	        }  
//		    return new String(new Base64().encode(b));
		 
		  }
	 
	
	 
	 /**
	  * 对象解码
	 * @param <T>
	 * @param <T>
	  * @param obj
	  * @param clazz
	  * @return
	  */
	 public static  Object  deserialization(String obj) {
//			try {
//			Kryo kryo = pool.borrow();
//	        ByteArrayInputStream bais;
//			bais = new ByteArrayInputStream(obj.getBytes("ISO8859-1"));
//	                //new Base64().decode(obj));
//	        Input input = new Input(bais);
//	        return kryo.readClassAndObject(input);
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
		 
		 
		 ByteArrayInputStream bis = null;  
	        ObjectInputStream ois = null;  
	        try {  
	            bis = new ByteArrayInputStream(obj.getBytes("ISO8859-1"));  
	            ois = new ObjectInputStream(bis);  
	            return ois.readObject();  
	        } catch (Exception e) {  
	            throw new RuntimeException("deserialize session error", e);  
	        } finally {  
	            try {  
	                ois.close();  
	                bis.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }
	
	
	
	
}
