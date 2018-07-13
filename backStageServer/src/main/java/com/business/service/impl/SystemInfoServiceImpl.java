package com.business.service.impl;

import Entry.ActiveCount;
import com.business.dao.*;
import com.business.service.SystemInfoService;
import com.hxy.nettygo.result.base.tools.CastUtil;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.hxy.nettygo.result.base.tools.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SystemInfoServiceImpl implements SystemInfoService{
    @Autowired
    UserDao userDao;

    @Autowired
    ActiveDao activeDao;

    @Autowired
    LoginTimeDao loginTimeDao;

    @Autowired
    OnlineDao onlineDao;

    @Autowired
    MemoryDao memoryDao;

    @Autowired
    OtherDao otherDao;

    @Autowired
    DruidDao druidDao;

    /**
     * 当前注册人数
     * @param gameName
     * @return
     */
    @Override
    public int getNowUserCount(String gameName) {
        Map res = userDao.selectNowUserCount(gameName);
        if(ObjectUtil.isNotNull(res)){
            return (Integer) res.get("count");
        }
        return 0;
    }

    /**
     * 七天活跃人数
     * @return
     */
    @Override
    public List<ActiveCount> selectSevenDayActiveCount(String gameName,String time) {
        return activeDao.selectSevenDayActiveCount(gameName,time);
    }

    /**
     * 七天登录
     * @return
     */
    @Override
    public List<Map> selectSevenDayLoginTime(String gameName,String time) {
        List<Map> res = loginTimeDao.selectSevenDayLoginTime(gameName,time);
        return res;
    }

    /**
     * 统计每天活跃人数
     * @param gameName
     */
    @Override
    public void statisOnlineToDayActive(String gameName,String time) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String paramDate = df.format(date);
        int count = loginTimeDao.selectOnlineMaxCountByDay(paramDate,gameName);
        ActiveCount activeCount = new ActiveCount();
        activeCount.setActiveId(CastUtil.castString(SnowflakeIdWorker.getSnowflakeIdWorker().nextId()));
        activeCount.setActiveCount(count);
        activeCount.setCreateTime(time);
        activeCount.setGameName(gameName);
        activeDao.addActive(activeCount);
    }

    @Override
    public List<Map> selectOnlineCountInfo(String gameName,String time) {
        return onlineDao.selectOnlineCountInfo(gameName,time);
    }

    @Override
    public List<Map> selectLoginTimenInterval(String gameName,String time) {
        return loginTimeDao.selectLoginTimenInterval(gameName,time);
    }

    @Override
    public List<Map> selectJVMInfo(String gameName, String time) {
        return memoryDao.selectJVMInfoByNameAndTime(gameName,time);
    }

    @Override
    public int updateShareOpenByGame(String dataBaseName,String gameName,String configName, boolean isOpen) {
        return otherDao.updateSystemInfoByName(dataBaseName,gameName,configName,isOpen);
    }

    @Override
    public Map selectSystemInfoByName(String dataBaseName, String gameName, String configName) {
        return otherDao.selectSomeSystemInfoByName(dataBaseName,gameName,configName);
    }

    @Override
    public List<Map> selectDruidInfoList() {
        return druidDao.selectDruidInfoList();
    }

    @Override
    public int deleteDruidInfo(String id) {
        return druidDao.deleteDruidInfoById(id);
    }
}
