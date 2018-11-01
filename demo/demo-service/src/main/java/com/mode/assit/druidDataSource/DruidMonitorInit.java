package com.mode.assit.druidDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import nafos.core.util.SpringApplicationContextHolder;

/**
 * @Author 黄新宇
 * @Date 2018/5/22 上午11:33
 * @Description 初始化监控，每1小时50分钟获取一次数据
 **/
public class DruidMonitorInit {

    public void init(){
        SpringApplicationContextHolder.getContext().getBean(DruidDataSource.class).setStatLogger(SpringApplicationContextHolder.getContext().getBean(MyDruidStatLogger.class));
        SpringApplicationContextHolder.getContext().getBean(DruidDataSource.class).setTimeBetweenLogStatsMillis(60*1000*60*2-600000);
    }
}
