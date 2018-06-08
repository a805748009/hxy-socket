package com.business.service;


import com.result.base.entry.backStageBean.ActiveCount;

import java.util.List;
import java.util.Map;

public interface SystemInfoService {
    int getNowUserCount(String gameName);

    List<ActiveCount> selectSevenDayActiveCount(String gameName, String time);

    List<Map> selectSevenDayLoginTime(String gameName, String time);

    void statisOnlineToDayActive(String gameName, String time);

    List<Map> selectOnlineCountInfo(String gameName, String time);

    List<Map> selectLoginTimenInterval(String gameName, String time);

    List<Map> selectJVMInfo(String gameName, String time);

    int updateShareOpenByGame(String dataBaseName, String gameName, String configName, boolean isOpen);

    Map selectSystemInfoByName(String dataBaseName, String gameName, String configName);

    List<Map> selectDruidInfoList();

    int deleteDruidInfo(String id);
}
