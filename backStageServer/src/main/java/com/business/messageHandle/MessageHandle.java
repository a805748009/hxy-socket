package com.business.messageHandle;

import com.business.dao.*;
import com.business.service.SystemInfoService;
import com.hxy.nettygo.result.base.annotation.QueueListener;
import com.hxy.nettygo.result.base.annotation.Route;
import com.hxy.nettygo.result.base.entry.backStageBean.*;
import com.hxy.nettygo.result.base.tools.DateUtil;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author 黄新宇
 * @Date 2018/5/11 上午9:29
 * @Description TODO
 **/
@Route
public class MessageHandle {

    @Autowired
    ActiveDao activeDao;

    @Autowired
    LoginTimeDao loginTimeDao;

    @Autowired
    MemoryDao memoryDao;

    @Autowired
    OnlineDao onlineDao;

    @Autowired
    AdminDao adminDao;

    @Autowired
    SystemInfoService systemInfoService;

    @Autowired
    DruidMonitorDao druidMonitorDao;

    @QueueListener("onlineCount")
    public void setOnlineCount(OnlineCount onlineCount) {
        onlineDao.addOnlineCount(onlineCount);
    }

    @QueueListener("memory")
    public void setMemory(JVMmemory jvmmemory) {
        memoryDao.addMemory(jvmmemory);
    }

    @QueueListener("userCount")
    public void userCount(UserCount userCount) {
        adminDao.updateGameUserCount(userCount);
    }
    @QueueListener("loginTime")
    public void setLoginTime(LoginTime lt) {
        loginTimeDao.addLoginTime(lt);
    }

    @QueueListener("loginTimeList")
    public void setLoginTimeList(LoginTimeList lts) {
        StringBuffer param = new StringBuffer();
        for (int x = 0; x < lts.getLoginTime().size(); x++) {
            LoginTime time = lts.getLoginTime().get(x);
            if (x == lts.getLoginTime().size() - 1) {
                param.append("('");
                param.append(time.getLoginId() + "','");
                param.append(time.getLoginUserId() + "','");
                param.append(time.getLoginTime() + "','");
                param.append(time.getGameName() + "')");
            } else {
                param.append("('");
                param.append(time.getLoginId() + "','");
                param.append(time.getLoginUserId() + "','");
                param.append(time.getLoginTime() + "','");
                param.append(time.getGameName() + "'),");
            }
        }
        System.out.println(param.toString());
        loginTimeDao.addLoginTimeList(param.toString());
    }

    @QueueListener("druidMonitor")
    public void setDruidMonitor(DruidMonitor druidMonitor) {
        DruidMonitor newDruidMonitor = druidMonitorDao.getDruidMonitor(druidMonitor.getSqlStr());
        if (ObjectUtil.isNull(newDruidMonitor)) {
            druidMonitorDao.insertDruidMonitor(druidMonitor);
        } else {
            if (newDruidMonitor.getConcurrentMax() > druidMonitor.getConcurrentMax())
                druidMonitor.setConcurrentMax(newDruidMonitor.getConcurrentMax());
            if (newDruidMonitor.getExecuteMillisMax() > druidMonitor.getExecuteMillisMax())
                druidMonitor.setExecuteMillisMax(newDruidMonitor.getExecuteMillisMax());
            druidMonitorDao.updateSqlMonitor(druidMonitor);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void addActiveUser() {
        String nowTime = DateUtil.getNowTime();
        //TODO online统计成日活跃
        systemInfoService.statisOnlineToDayActive("flyup", nowTime);
        systemInfoService.statisOnlineToDayActive("nocandie", nowTime);
    }
}
