package com.controller.socket;

import nafos.core.util.JsonUtil;

import java.io.UnsupportedEncodingException;

public class BeanJsonToBinaryUtil {
    public static byte[] to(Object object){
        try {
            return JsonUtil.toJson(object).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return JsonUtil.toJson(object).getBytes();
    }
}
