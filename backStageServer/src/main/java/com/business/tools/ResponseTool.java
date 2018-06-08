package com.business.tools;

import java.util.HashMap;
import java.util.Map;

public class ResponseTool {

    public static Object newErrorResponse(String errorMsg){
        Map<String,Object> res = new HashMap<>();
        res.put("success",false);
        res.put("errorMsg",errorMsg);
        return res;
    }

    public static Object newObjectResponse(Object obj){
        Map<String,Object> res = new HashMap<>();
        res.put("success",true);
        res.put("data",obj);
        return res;
    }
}
