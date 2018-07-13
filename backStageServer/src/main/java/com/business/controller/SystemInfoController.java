package com.business.controller;

import com.business.service.SystemInfoService;
import com.business.tools.ResponseTool;
import com.hxy.nettygo.result.base.annotation.Nuri;
import com.hxy.nettygo.result.base.annotation.Route;
import com.hxy.nettygo.result.base.tools.CastUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Route
@Nuri(uri = "/SystemInfo")
public class SystemInfoController {
    @Autowired
    SystemInfoService systemInfoService;

    @Nuri(uri = "/getNowUserCount", method = "GET", type = "JSON")
    public Object getNowUserCount(Map<String, Object> map) {
        String gameName = map.get("gameName").toString();
        int count = systemInfoService.getNowUserCount(gameName);
        return ResponseTool.newObjectResponse(count);
    }

    @Nuri(uri = "/getSevenActiveCount", method = "GET", type = "JSON")
    public Object getSevenActiveCount(Map<String, Object> map) {
        String gameName = map.get("gameName").toString();
        String time = map.get("time").toString();
        return ResponseTool.newObjectResponse(systemInfoService.selectSevenDayActiveCount(gameName, time));
    }

    @Nuri(uri = "/getSevenLoginTimeInfo", method = "GET", type = "JSON")
    public Object getSevenLoginTimeInfo(Map<String, Object> map) {
        String gameName = map.get("gameName").toString();
        String time = map.get("time").toString();
        return ResponseTool.newObjectResponse(systemInfoService.selectSevenDayLoginTime(gameName, time));
    }

    @Nuri(uri = "/getOnlineCountInfo", method = "GET", type = "JSON")
    public Object getOnlineCountInfo(Map<String, Object> map) {
        String gameName = map.get("gameName").toString();
        String time = map.get("time").toString();
        return ResponseTool.newObjectResponse(systemInfoService.selectOnlineCountInfo(gameName, time));
    }

    @Nuri(uri = "/getLoginTimenInterval", method = "GET", type = "JSON")
    public Object getLoginTimenInterval(Map<String, Object> map) {
        String gameName = map.get("gameName").toString();
        String time = map.get("time").toString();
        return ResponseTool.newObjectResponse(systemInfoService.selectLoginTimenInterval(gameName, time));
    }

    @Nuri(uri = "/getJVMInfo", method = "GET", type = "JSON")
    public Object getJVMInfo(Map<String, Object> map) {
        String gameName = map.get("gameName").toString();
        String time = map.get("time").toString();
        return ResponseTool.newObjectResponse(systemInfoService.selectJVMInfo(gameName, time));
    }

    @Nuri(uri = "/updateShareOpen", method = "POST", type = "JSON")
    public Object updateShareOpen(Map<String, Object> map) {
        String gameName = map.get("gameName").toString();
        Boolean isOpen = CastUtil.castBoolean(map.get("isOpen"));
        int count = systemInfoService.updateShareOpenByGame("noBody", gameName, "showSharePanel", isOpen);
        if (count >= 1) {
            return ResponseTool.newObjectResponse(systemInfoService.selectSystemInfoByName("noBody", gameName, "showSharePanel"));
        }
        else{
            return ResponseTool.newErrorResponse("更新失败...");
        }
    }

    @Nuri(uri = "/getShareOpen", method = "GET", type = "JSON")
    public Object getShareOpen(Map<String, Object> map) {
        String gameName = map.get("gameName").toString();
        return ResponseTool.newObjectResponse(systemInfoService.selectSystemInfoByName("noBody", gameName, "showSharePanel"));
    }

    @Nuri(uri = "/getDruidInfoListPage", method = "GET", type = "JSON")
    public Object getDruidInfoListPage(Map<String, Object> map) {
        return ResponseTool.newObjectResponse(systemInfoService.selectDruidInfoList());
    }

    @Nuri(uri = "/deleteDruidInfoById", method = "POST", type = "JSON")
    public Object deleteDruidInfoById(Map<String, Object> map) {
        String druidId = map.get("id").toString();
        return ResponseTool.newObjectResponse(systemInfoService.deleteDruidInfo(druidId));
    }
}
