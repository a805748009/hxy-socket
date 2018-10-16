package nafos.core.entry;


/**
 * @Author 黄新宇
 * @Date 2018/5/22 上午11:21
 * @Description TODO
 **/
public class DruidMonitor {


    protected String id;

    protected String sqlStr;

    protected long executeCount;

    protected long executeMillisMax;

    protected long executeMillisTotal;

    protected long executeErrorCount;

    protected long runningCount;

    protected long concurrentMax;

    public DruidMonitor() {
    }

    public DruidMonitor(String uri, String id, String sqlStr, long executeCount, long executeMillisMax, long executeMillisTotal, long executeErrorCount, long runningCount, long concurrentMax) {
        this.id = id;
        this.sqlStr = sqlStr;
        this.executeCount = executeCount;
        this.executeMillisMax = executeMillisMax;
        this.executeMillisTotal = executeMillisTotal;
        this.executeErrorCount = executeErrorCount;
        this.runningCount = runningCount;
        this.concurrentMax = concurrentMax;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sql) {
        this.sqlStr = sql;
    }

    public long getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(long executeCount) {
        this.executeCount = executeCount;
    }

    public long getExecuteMillisMax() {
        return executeMillisMax;
    }

    public void setExecuteMillisMax(long executeMillisMax) {
        this.executeMillisMax = executeMillisMax;
    }

    public long getExecuteMillisTotal() {
        return executeMillisTotal;
    }

    public void setExecuteMillisTotal(long executeMillisTotal) {
        this.executeMillisTotal = executeMillisTotal;
    }

    public long getExecuteErrorCount() {
        return executeErrorCount;
    }

    public void setExecuteErrorCount(long executeErrorCount) {
        this.executeErrorCount = executeErrorCount;
    }

    public long getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(long runningCount) {
        this.runningCount = runningCount;
    }

    public long getConcurrentMax() {
        return concurrentMax;
    }

    public void setConcurrentMax(long concurrentMax) {
        this.concurrentMax = concurrentMax;
    }

    @Override
    public String toString() {
        return "DruidMonitor{" +
                "id='" + id + '\'' +
                ", sqlStr='" + sqlStr + '\'' +
                ", executeCount=" + executeCount +
                ", executeMillisMax=" + executeMillisMax +
                ", executeMillisTotal=" + executeMillisTotal +
                ", executeErrorCount=" + executeErrorCount +
                ", runningCount=" + runningCount +
                ", concurrentMax=" + concurrentMax +
                '}';
    }
}
