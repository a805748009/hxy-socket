package com.business.dao;


import com.result.base.entry.backStageBean.DruidMonitor;
import org.springframework.stereotype.Repository;

/**
 * @Author 黄新宇
 * @Date 2018/5/22 下午12:22
 * @Description TODO
 **/
@Repository
public interface DruidMonitorDao {

    void updateSqlMonitor(DruidMonitor druidMonitor);

    DruidMonitor getDruidMonitor(String sql);

    int insertDruidMonitor(DruidMonitor druidMonitor);
}
