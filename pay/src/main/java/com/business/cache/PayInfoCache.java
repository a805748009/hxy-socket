package com.business.cache;

import com.business.controller.PayController;
import com.hxy.nettygo.result.base.tools.JarToolUtil;
import net.sf.json.JSONObject;

import java.io.*;

/**
 * @Author 黄新宇
 * @Date 2018/7/11 下午2:36
 * @Description TODO
 **/
public class PayInfoCache {
    public static JSONObject jsonObject = null;
    static{
        StringBuffer content = new StringBuffer();
        try {
            File file = new File(PayController.class.getClassLoader().getResource("payInfo.json").getPath());
            if (!file.exists()) {
                file = new File(JarToolUtil.getJarDir()+"/conf/payInfo.json");
            }
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    content.append(lineTxt);
                }
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            System.out.println("Cannot find the file specified!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file content!");
            e.printStackTrace();
        }
        jsonObject = JSONObject.fromObject(content.toString());
    }
}
