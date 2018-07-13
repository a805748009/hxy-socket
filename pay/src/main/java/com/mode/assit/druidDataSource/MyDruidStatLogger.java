package com.mode.assit.druidDataSource;

import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.DruidDataSourceStatLoggerAdapter;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import com.alibaba.druid.stat.JdbcSqlStatValue;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.hxy.nettygo.result.base.entry.backStageBean.DruidMonitor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author 黄新宇
 * @Date 2018/5/14 下午3:03
 * @Description TODO
 **/
@Component
public class MyDruidStatLogger extends DruidDataSourceStatLoggerAdapter implements DruidDataSourceStatLogger {
    private static Log LOG    = LogFactory.getLog(MyDruidStatLogger.class);

    private static List<DruidMonitor> list = new ArrayList<>();

    private Log        logger = LOG;
    public MyDruidStatLogger(){
        this.configFromProperties(System.getProperties());
    }
    @Override
    public void configFromProperties(Properties properties) {
        String property = properties.getProperty("druid.stat.loggerName");
        if (property != null && property.length() > 0) {
            setLoggerName(property);
        }
    }
    public Log getLogger() {
        return logger;
    }

    @Override
    public void setLoggerName(String loggerName) {
        logger = LogFactory.getLog(loggerName);
    }

    @Override
    public void setLogger(Log logger) {
        if (logger == null) {
            throw new IllegalArgumentException("logger can not be null");
        }
        this.logger = logger;
    }

    public boolean isLogEnable() {
        return true;
    }

    public void log(String value) {
        logger.info(value);
    }
    @Override
    public void log(DruidDataSourceStatValue statValue) {
        list.clear();
        if (statValue.getSqlList().size() > 0) {
            for (JdbcSqlStatValue sqlStat : statValue.getSqlList()) {
                DruidMonitor dm = new DruidMonitor();
                dm.setSqlStr( sqlStat.getSql().replaceAll("\\n","").replaceAll("\\t",""));

                if (sqlStat.getExecuteCount() > 0) {
                    dm.setExecuteCount(sqlStat.getExecuteCount());
                    dm.setExecuteMillisMax(sqlStat.getExecuteMillisMax());
                    dm.setExecuteMillisTotal(sqlStat.getExecuteMillisTotal());
                }

                long executeErrorCount = sqlStat.getExecuteErrorCount();
                if (executeErrorCount > 0) {
                    dm.setExecuteErrorCount(executeErrorCount);
                }
                int runningCount = sqlStat.getRunningCount();
                if (runningCount > 0) {
                    dm.setRunningCount(runningCount);
                }
                int concurrentMax = sqlStat.getConcurrentMax();
                if (concurrentMax > 0) {
                    dm.setConcurrentMax(concurrentMax);
                }
                list.add(dm);
            }

        }
    }


    public static List<DruidMonitor> getList(){
        return list;
    }
}
