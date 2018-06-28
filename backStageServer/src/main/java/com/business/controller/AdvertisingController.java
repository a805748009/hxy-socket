package com.business.controller;

import com.business.service.AdvertisingService;
import com.business.tools.ResponseTool;
import com.result.base.annotation.Nuri;
import com.result.base.annotation.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Route
@Nuri(uri = "/Advertising")
public class AdvertisingController {
    @Autowired
    AdvertisingService advertisingService;

    @Nuri(uri = "/getAllAdvertising", method = "GET", type = "JSON")
    public Object getAllAdvertising(Map<String, Object> map) {
        List<Map> res =  advertisingService.selectAllAvertising();
        return ResponseTool.newObjectResponse(res);
    }

    @Nuri(uri = "/updateAdvertisingById", method = "POST", type = "JSON")
    public Object updateAdvertisingById(Map<String, Object> map) {
        String id = map.get("id").toString();
        String buttonImg = map.get("buttonImg").toString();
        String groundImg = map.get("groundImg").toString();
        String comment = map.get("comment").toString();
        int count = advertisingService.updateAvertising(id,buttonImg,groundImg,comment);
        if(count>0){
            return ResponseTool.newObjectResponse(count);
        }
        return ResponseTool.newErrorResponse("更新失败，请检查数据是否错误");
    }

    @Nuri(uri = "/deleteAdvertisingById", method = "POST", type = "JSON")
    public Object deleteAdvertisingById(Map<String, Object> map) {
        String id = map.get("id").toString();
        int count = advertisingService.deleteAdvertising(id);
        if(count>0){
            return ResponseTool.newObjectResponse(count);
        }
        return ResponseTool.newErrorResponse("删除失败，请检查数据是否错误");
    }

    @Nuri(uri = "/addAdvertising", method = "POST", type = "JSON")
    public Object addAdvertising(Map<String, Object> map) {
        String buttonImg = map.get("buttonImg").toString();
        String groundImg = map.get("groundImg").toString();
        String comment = map.get("comment").toString();
        int count = advertisingService.addAdvertising(buttonImg,groundImg,comment);
        if(count>0){
            return ResponseTool.newObjectResponse(count);
        }
        return ResponseTool.newErrorResponse("插入新信息失败，请检查数据是否错误");
    }
}
