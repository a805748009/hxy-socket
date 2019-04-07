package nafos.core.monitor;

import kotlinx.coroutines.experimental.GlobalScope;
import nafos.core.cache.LongAdderMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Classname RunWatch
 * @Description 记录每个方法的运行时间
 * @Date 2019/2/28 18:02
 * @Created by xinyu.huang
 */
public class RunWatch {

    private static Logger logger = LoggerFactory.getLogger(RunWatch.class);

    private static boolean openRunWatch = false;

    protected String DEFULTNAME = "defult_name";

    protected String methodName ;

    protected long startTime;

    protected static LongAdder totalTime = new LongAdder();

    protected final static LinkedList<ArrayList<RunWatch.RunInfo>> infoList = new LinkedList<>();

    protected static  LongAdderMap<String,LongAdder> runInfoMap = new LongAdderMap<>();

    protected static  LongAdderMap<String,LongAdder> countMap = new LongAdderMap<>();

    public RunWatch() {
    }

    public RunWatch(String name) {
        DEFULTNAME = name;
    }

    public  void run(){
        run(DEFULTNAME);
    }

    public void run(String name){
        startTime = System.currentTimeMillis();
        methodName = name;
    }

    public static void openRunWatch(){
        openRunWatch = true;
    }

    public static boolean getRunWatchStatus(){
        return openRunWatch ;
    }


    public long stop(){
        long time = System.currentTimeMillis() - startTime;
        if(openRunWatch){
            runInfoMap.incr(methodName,time);
            countMap.incr(methodName);
            totalTime.add(time);
        }
        return time;
    }

    public static RunWatch init(){
        RunWatch runWatch = new RunWatch();
        runWatch.run();
        return runWatch;
    }

    public static RunWatch init(String name){
        RunWatch runWatch = new RunWatch();
        runWatch.run(name);
        return runWatch;
    }

    /**
     * 获取当前的统计字符串
     * @return
     */
    public static String infoStr(){
        StringBuilder sb = new StringBuilder();
        sb.append("============开始打印方法运行时间统计============\n");
        sb.append( "                                                    " );
        sb.append( "name                                                                 count         totalTime         averageTime       proportion\n" );
        for (Map.Entry<String, LongAdder> e : runInfoMap.asMap().entrySet()) {
            long count =  countMap.get( e.getKey() ).longValue();
            long average = e.getValue().longValue() / count; //平均时间
            long proportion =  totalTime.longValue() == 0?100:e.getValue().longValue()*100/totalTime.longValue(); //总时间百分比
            sb.append( "                                                    " );
            sb.append( e.getKey()+"|");
            for(int i =0;i<70-e.getKey().length();i++){
                sb.append(" ");
            }
            sb.append(count+"|                "+e.getValue()+"|               "+average+"|              "+proportion+"|\n");
        }
        return sb.toString();
    }

    /**
     * 打印统计
     */
    public static void print(){
        logger.info(infoStr());
    }

    /**
     * 定时打印  方法过期，建议使用fiberDo.kt中的协程来实现
     * @param millisecond
     */
    @Deprecated
    public static void cronPrint(long millisecond){
        new Thread(()->{
            Thread.currentThread().setName( "RunWatchMonitor");
            for(;;){
                try {
                    Thread.sleep(millisecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                print();
            }
        }).start();
    }

    public static RunInfo info(String name){
        long totalTime = runInfoMap.get(name).longValue();
        long count =  countMap.get( name ).longValue();
        long average = totalTime / count; //平均时间
        long proportion = totalTime*100/RunWatch.totalTime.longValue(); //总时间百分比
        return new RunInfo(name,count,totalTime,average,proportion);
    }

    /**
     * 获取本周的监控信息
     * @return
     */
    public static ArrayList<RunInfo> info(){
        ArrayList<RunInfo> list = new ArrayList<>();
        for (Map.Entry<String, LongAdder> e : runInfoMap.asMap().entrySet()) {
            long totalTime = e.getValue().longValue();
            long count = countMap.get(e.getKey()).longValue();
            long average = totalTime / count; //平均时间
            long proportion = totalTime * 100 / RunWatch.totalTime.longValue(); //总时间百分比
            list.add(new RunInfo(e.getKey(), count, totalTime, average, proportion));
        }
        return list;
    }

    /**
     * 获取之前一个月的监控信息
     * @return
     */
    public static LinkedList<ArrayList<RunWatch.RunInfo>> getMonthInfo(){
        return  infoList;
    }

    /**
     * 添加这周的监控信息到infos
     * @return
     */
    public static synchronized void addCurWeekInfo(ArrayList<RunInfo> runInfos){
        if(infoList.size() >= 4){
            infoList.removeFirst();
        }
        infoList.add(runInfos);
    }

    /**
     * 重置所有的记录，重新开始记录
     */
    public static void resetInfo(){
        totalTime.reset();
        runInfoMap.clear();
        countMap.clear();
    }














    public static class RunInfo{
        private String name;
        private long count;
        private long totalTime;
        private long average;
        private long proportion;

        public RunInfo(String name, long count, long totalTime, long average, long proportion) {
            this.name = name;
            this.count = count;
            this.totalTime = totalTime;
            this.average = average;
            this.proportion = proportion;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public long getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(long totalTime) {
            this.totalTime = totalTime;
        }

        public long getAverage() {
            return average;
        }

        public void setAverage(long average) {
            this.average = average;
        }

        public long getProportion() {
            return proportion;
        }

        public void setProportion(long proportion) {
            this.proportion = proportion;
        }
    }


}
