package nafos.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import nafos.core.util.CastUtil;
import nafos.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * ClassName:DateUtil
 * Function: TODO ADD FUNCTION.
 * Date:     2017年8月23日 下午1:52:45
 * @author   HXY
 */
public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat sdf_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat sdf_format = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final SimpleDateFormat sdf_yyyymm = new SimpleDateFormat("yyyyMM");

    private static final SimpleDateFormat sdf_format_month = new SimpleDateFormat("yyyy-MM");

    private static final SimpleDateFormat sdf_format_hm = new SimpleDateFormat("HH:mm");

    private static final SimpleDateFormat sdf_utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final SimpleDateFormat sdf_yymmdd = new SimpleDateFormat("yyMMdd");

    private static final SimpleDateFormat sdf_yyyymmdd = new SimpleDateFormat("yyyyMMdd");

    /**
     * 将字符串类型的时间转化为yyyy-MM-dd的格式（结果同时字符串）
     *
     * @param time
     * @return
     */
    public static String formatTime(String time) {
        try {
            return sdf_day.format(sdf_day.parse(time));
        } catch (ParseException e) {
            log.warn("{}", e);

        }
        return "";
    }

    /**
     * 将字符串类型的时间yyyy-MM-dd HH:mm:ss转化为HH:mm:00的格式（结果同时字符串）
     *
     * @param time
     * @return
     */
    public static String formatHmsTime(String time) {
        try {
            Date date = sdf.parse(time);
            return sdf_format_hm.format(date) + ":00";
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return "";
    }

    /**
     * 秒(int)转HH时mm分ss秒
     *
     * @param learnTime
     * @return
     */
    public static String formatTimeHms(int learnTime) {

        if(learnTime == 0) {
            return "0小时0分0秒";
        } else {
            int hour = learnTime / 3600;
            int minutes = (learnTime - hour * 3600) / 60;
            int seconds = (learnTime - hour * 3600) % 60;

            String m = minutes + "";
            String s = seconds + "";
            if(minutes < 10) {
                m = "0" + minutes;
            }
            if(seconds < 10) {
                s = "0" + seconds;
            }
            return hour + "小时" + m + "分" + s + "秒";
        }

    }

    /**
     * 秒(int)转HH时mm分ss秒
     *
     * @param learnTime
     * @return
     */
    public static String formatTimeHmsEng(int learnTime) {
        if(learnTime == 0) {
            return "0h 0m 0秒";
        } else {
            int hour = learnTime / 3600;
            int minutes = (learnTime - hour * 3600) / 60;
            int seconds = (learnTime - hour * 3600) % 60;

            String m = minutes + "";
            String s = seconds + "";
            if(minutes < 10) {
                m = "0" + minutes;
            }
            if(seconds < 10) {
                s = "0" + seconds;
            }
            return hour + "h " + m + "m " + s + "s";
        }
    }

    /**
     * long型数据 转化为B、KB、MB或者GB
     */
    public static String formatKMGB(long data) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if(data < 1024) {
            return CastUtil.castString(data) + "B";
        } else {
            data = data / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if(data < 1024) {
            return CastUtil.castString(data) + "KB";
        } else {
            data = data / 1024;
        }
        if(data < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            data = data * 100;
            return CastUtil.castString((data / 100)) + "." + CastUtil.castString((data % 100)) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            data = data * 100 / 1024;
            return CastUtil.castString((data / 100)) + "." + CastUtil.castString((data % 100)) + "GB";
        }
    }

    /**
     * long型数据 (B)转化为(MB),四舍五入
     */
    public static long formatMB(long data) {
        if(data < 1024) {
            return 0;
        } else {
            // 转化为KB
            data = data / 1024;
        }
        if(data < 1024) {
            if(1024 / data <= 2) {
                return 1;
            } else {
                return 0;
            }
        } else {
            // 转为MB
            long result = data / 1024;
            if(data % 1024 > 512) {
                result = result + 1;
            }
            return result;
        }
    }

    public static long getRank(int order, int level, int total) {
        long rank = CastUtil.castLong(Math.pow(10, (total - level + 2)));
        return order * rank;
    }

    /**
     * 分类总排序算法2： 1级:10100 2级:10101
     *
     * @param prank
     *            上次总排序
     * @param order
     *            当前排序
     * @param level
     *            当前层级
     * @param total
     *            总层级
     * @return
     */
    public static long getRank2(long prank, long order, int level, int total) {
        return prank + CastUtil.castLong(Math.pow(10, (total - level + 2))) * order;
    }

    public static long getMaxId(long maxId, long Pid) {
        if(maxId <= 0) {
            if(Pid == 0) {
                maxId = 10;
            } else {
                maxId = Pid * 100;
            }
        }
        maxId++;

        return maxId;
    }

    public static int getLevel(long num) {
        String str = CastUtil.castString(num);

        return str.length();
    }

    public static Object[] getObj(Object[] objArray, int len) {
        if(objArray == null) {
            objArray = new Object[len];
            for(int i = 0; i < len; i++) {
                objArray[i] = "";
            }
        }

        return objArray;
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss 字符串
     */
    public static String getNowTime() {
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd HH:mm 字符串
     */
    public static String getNowTimemm() {
        return sdf_mm.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM-dd 字符串
     */
    public static String getNowTimeDay() {
        return sdf_day.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyyMMddHHmmss 字符串
     */
    public static String getNowFormat() {
        return sdf_format.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyyMM 字符串
     */
    public static String getNowYYYYMM() {
        return sdf_yyyymm.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyy-MM 字符串
     */
    public static String getNowYearAndMonth() {
        return sdf_format_month.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyMMdd 字符串
     */
    public static String getNowYYMMDD() {
        return sdf_yymmdd.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return yyyyMMdd 字符串
     */
    public static String getNowYYYYMMDD() {
        return sdf_yyyymmdd.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return HH:mm 字符串
     */
    public static String getNowHm() {
        return sdf_format_hm.format(new Date());
    }

    /**
     * 计算time跟当前时间相差多久 超过24小时 直接返回空字符串 视频评论时间处理指定方法,请勿修改
     *
     * @param time
     * @return
     */
    public static String getVideoCommentDistanceTime(String time) {
        if(ObjectUtil.isNull(time)) {
            return "";
        }
        Date one;
        Date two = new Date();
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = sdf.parse(time);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if(time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            return "";
        }
        String timeStr = "";
        if(day == 0 && hour < 24) {
            if(hour == 0 && min == 0) {
                timeStr = sec + "秒";
            } else if(hour == 0) {
                timeStr = min + "分钟";
            } else {
                timeStr = hour + "小时";
            }
        }
        return timeStr;
    }

    /**
     * 俩个时间相差多少分钟
     */
    public static double calTimeMillis(String str1, String str2) {
        Date one;
        Date two;
        double min = 0;
        try {
            one = sdf.parse(str1);
            two = sdf.parse(str2);
            Calendar dateOne = Calendar.getInstance(), dateTwo = Calendar.getInstance();
            dateOne.setTime(one);
            dateTwo.setTime(two);
            long timeOne = dateOne.getTimeInMillis();
            long timeTwo = dateTwo.getTimeInMillis();
            min = (timeOne - timeTwo) / (1000 * 60);// 转化minute
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return min;
    }

    /**
     * 俩个时间相差多少秒
     */
    public static long calMinTime(String str1, String str2) {
        Date one;
        Date two;
        long diff = 0;
        try {
            one = sdf.parse(str1);
            two = sdf.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            if(time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return diff / 1000;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1
     *            时间参数 1 格式：1990-01-01 12:00:00
     * @param str2
     *            时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(String str1, String str2) {
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = sdf.parse(str1);
            two = sdf.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if(time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }

    /**
     * 两个时间相差距离多少小时多少分多少秒
     *
     * @param str1
     *            时间参数 1 格式：1990-01-01 12:00:00
     * @param str2
     *            时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx小时xx分xx秒
     */
    public static String getHourDistanceTime(String str1, String str2) {
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = sdf.parse(str1);
            two = sdf.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if(time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return hour + "小时" + min + "分" + sec + "秒";
    }

    /**
     * 两个时间相差距离多少小时多少分多少秒
     *
     * @param time1
     *            时间参数 1 格式：1478163361309 时间戳
     * @param time2
     *            时间参数 2 格式：1478163361309 时间戳
     * @return String 返回值为：xx小时xx分xx秒
     */
    public static String getHourDistanceTimestamp(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            long diff;
            if(time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            log.warn("{}", e);
        }
        return hour + "小时" + min + "分" + sec + "秒";
    }

    /**
     * 特殊方法 得到当前时间的前面的天 返回格式yyyyMMdd
     */
    public static String beforDay(int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            Calendar cd = Calendar.getInstance();
            cd.setTime(new Date());
            cd.add(Calendar.DATE, -n);// 增加一天
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 增加天数
     *
     * @param s
     * @param n
     * @return
     */
    public static String addDay(String s, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);// 增加一天
            // cd.add(Calendar.MONTH, n);//增加一个月
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static String addHour(String s, int n) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf_format.parse(s));
            cd.add(Calendar.HOUR, n);// 增加小时
            return sdf_format.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static String addSecond(String s, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cd = Calendar.getInstance();
        try {
            cd.setTime(sdf.parse(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cd.add(Calendar.SECOND, n);
        return sdf.format(cd.getTime());
    }

    public static String addHourFormatSDF(String s, int n) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.HOUR, n);// 增加小时
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static String addMonth(String s, int n) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf_day.parse(s));
            cd.add(Calendar.MONTH, n);// 增加一个月
            return sdf_day.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static String addMillis(String s, int n) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf_mm.parse(s));
            cd.add(Calendar.MINUTE, n);// 增加分钟
            return sdf_mm.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 提前分钟计算
     *
     * @param time
     *            yyyy-MM-dd HH:mm:ss
     * @param n
     *            1
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String reduceMillis(String time, int n) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(time));
            cd.add(Calendar.MINUTE, -n); // 分钟
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return time;
        }
    }

    /**
     * 推迟分钟计算
     *
     * @param time
     *            yyyy-MM-dd HH:mm:ss
     * @param n
     *            1
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String delayMillis(String time, int n) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(time));
            cd.add(Calendar.MINUTE, n); // 分钟
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return time;
        }
    }

    public static boolean checkDobleTime(String time1, String time2) {
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf_day.parse(time1));
            Calendar c2 = Calendar.getInstance();
            c2.setTime(sdf_day.parse(time2));
            if(c1.equals(c2)) {
                return true;
            } else {
                return c1.before(c2);
            }
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return false;
    }

    public static boolean checkDobleDay(String time1, String time2) {
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf_day.parse(time1));
            Calendar c2 = Calendar.getInstance();
            c2.setTime(sdf_day.parse(time2));
            return c1.before(c2);
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return false;
    }

    public static boolean checkDobleTimeMM(String time1, String time2) {
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf_mm.parse(time1));
            Calendar c2 = Calendar.getInstance();
            c2.setTime(sdf_mm.parse(time2));
            if(c1.equals(c2)) {
                return false;
            } else {
                return c1.before(c2);
            }
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return false;
    }

    public static boolean checkAfterTimeMM(String time1, String time2) {
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf_mm.parse(time1));
            Calendar c2 = Calendar.getInstance();
            c2.setTime(sdf_mm.parse(time2));
            return c1.after(c2);
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return false;
    }

    // 判断今天是否是本月的第一天
    public static boolean checkMonthOne() {
        Calendar c = Calendar.getInstance();
        // 得到本月的那一天
        @SuppressWarnings("static-access")
        int today = c.get(c.DAY_OF_MONTH);
        return today == 1 ? true : false;
    }

    // 得到上个月的年月数据 YYYYMM
    public static String getLastDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        return sdf_yyyymm.format(cal.getTime());
    }

    public static boolean isDateBefore(Date date2) {
        Date date1 = new Date();// 当前时间
        return date1.before(date2);
    }

    public static Date secondTime(Integer second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

    public static String formatDayTime(String time) {
        try {
            time = sdf_day.format(sdf_day.parse(time));
            return time.split("-")[1] + "-" + time.split("-")[2];
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return "";
    }

    public static String formatMonthTime(String time) {
        try {
            return sdf_format_month.format(sdf_format_month.parse(time));
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return "";
    }

    public static String usMonth(String time) {
        SimpleDateFormat sdf_sign_month = new SimpleDateFormat("MM");
        try {
            time = sdf_day.format(sdf_day.parse(time));
            time = time.split("-")[1];
            Date date = sdf_sign_month.parse(time);
            sdf_sign_month = new SimpleDateFormat("MMMMM", Locale.US);
            return sdf_sign_month.format(date);
        } catch (ParseException e) {
            log.warn("{}", e);

        }
        return "";
    }

    public static String formatDay(String time) {
        try {
            time = sdf_day.format(sdf_day.parse(time));
            return time.split("-")[2];
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return "";
    }

    public static String getWeekOfDate(String time) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf_day.parse(time));
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(w < 0) {
                w = 0;
            }
            return weekDays[w];
        } catch (ParseException e) {
            log.warn("{}", e);
        }
        return weekDays[0];
    }

    public static String getMaxYearDay(String st2) {
        Calendar cal = Calendar.getInstance();
        String st3 = cal.get(Calendar.YEAR) + "-12-31";
        if(checkDobleDay(st2, st3)) {
            return st2;
        } else {
            return st3;
        }
    }

    /**
     * 将时间戳转换成当前时间
     *
     * @param timestamp
     * @return
     */
    public static String getTimeStr(String timestamp) {
        Date date = new Date(new Long(timestamp));
        return sdf.format(date);
    }

    /**
     * 将当前时间转换成时间戳
     *
     * @param time
     * @return
     */
    public static long getTimeStamp(String time) {
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取上一个月的今天(如果是2017-03-30 则获取的是2017-02-29或者28)
     *
     * @param dateStr
     *            yyyy-MM-dd
     * @return
     */
    public static String getDateOfLastMonth(String dateStr) {
        try {
            Date date = sdf_day.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, -1);
            return sdf_day.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取指定日期的月份的最后一天
     *
     * @param dateStr
     * @return
     */
    public static String getLastDayOfMonth(String dateStr) {
        try {
            Date date = sdf_day.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 0);
            return sdf_day.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取UTC时间
     *
     * @param date
     * @return yyyy-MM-dd'T'HH:mm:ss'Z'
     */
    public static String formatUtcDate(Date date) {
        sdf_utc.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return sdf_utc.format(date);
    }

    /**
     * 获取UTC时间
     *
     * @param dateStr
     * @return yyyy-MM-dd'T'HH:mm:ss'Z'
     */
    public static String formatUtcDateStr(String dateStr) {
        try {
            Date d = sdf.parse(dateStr);
            sdf_utc.setTimeZone(new SimpleTimeZone(0, "GMT"));
            return sdf_utc.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将UTC时间转换为东八区时间
     *
     * @param UTCTime
     * @return
     */
    public static String getLocalTimeFromUTC(String UTCTime) {
        Date utcDate = null;
        String localTimeStr = null;
        try {
            sdf_utc.setTimeZone(TimeZone.getTimeZone("UTC"));
            utcDate = sdf_utc.parse(UTCTime);
            sdf.setTimeZone(TimeZone.getDefault());
            localTimeStr = sdf.format(utcDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localTimeStr;
    }

    /**
     * Java判断一个时间是否在另一个时间段内 比如：当时间在凌晨0点至0点5分之间程序不执行 也就是实现判断当前时间点是否在00:00:00至00:05:00之间
     *
     * @author lisheng
     * @date 2017年5月4日 上午10:30:03
     * @return boolean
     * @param strDate
     *            当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin
     *            开始时间 00:00:00
     * @param strDateEnd
     *            结束时间 00:05:00
     */
    public static boolean isInDate(String strDate, String strDateBegin, String strDateEnd) {
        // 截取当前时间时分秒
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        int strDateS = Integer.parseInt(strDate.substring(17, 19));

        // 截取开始时间时分秒
        int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
        int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));

        // 截取结束时间时分秒
        int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
        int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
        // 当前时间小时数在开始时间和结束时间小时数之间
        if((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
            if(strDateH > strDateBeginH && strDateH < strDateEndH) {
                return true;
            } else if(strDateH == strDateBeginH && strDateM > strDateBeginM && strDateH < strDateEndH) {

                return true;
            } else if(strDateH == strDateBeginH
                    && strDateM == strDateBeginM && strDateS > strDateBeginS && strDateH < strDateEndH) {
                return true;
            } else if(strDateH == strDateBeginH
                    && strDateM == strDateBeginM && strDateS == strDateBeginS && strDateH < strDateEndH) {
                return true;
            } else if(strDateH > strDateBeginH && strDateH == strDateEndH && strDateM < strDateEndM) {
                return true;
            } else if(strDateH > strDateBeginH && strDateH == strDateEndH && strDateM == strDateEndM && strDateS < strDateEndS) {
                return true;
            } else if(strDateH > strDateBeginH && strDateH == strDateEndH && strDateM == strDateEndM && strDateS == strDateEndS) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 将日期转换成字符串，格式为yyyy-MM-dd HH:mm:ss
     *
     * @author lisheng
     * @date 2017年7月11日 下午3:45:36
     * @return String
     * @param date
     */
    public static String formatDate(Date date) {
        if(date == null) return null;
        return sdf.format(date);
    }

    /**
     * 获取当前时间前一个小时的时间
     *
     * @author lisheng
     * @date 2017年7月12日 下午5:52:31
     * @return String 2017-07-12 16:56:30
     */
    public static String getBeforeOneHourFromNowDate() {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前时间n年后的时间
     *
     * @author lisheng
     * @date 2017年7月12日 下午10:28:45
     * @return String
     * @param nYear
     * @return
     */
    public static String getNYearFromNowDate(int nYear) {
        Calendar calendar = Calendar.getInstance();
        // calendar.add(Calendar.YEAR, nYear);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + nYear);
        return sdf.format(calendar.getTime());
    }

    /**
     * 将日期转换成字符串，格式为自定义
     *
     * @author lisheng
     * @date 2017年7月12日 下午5:58:01
     * @return String
     * @param date
     * @param formatStyle
     * @return
     */
    public static String formatDate(Date date, String formatStyle) {
        if(date == null || ObjectUtil.isNull(formatStyle)) return null;
        return new SimpleDateFormat(formatStyle).format(date);
    }

    /**
     * 将日期字符串转换成日期，只支持如下格式： yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
     */
    public static Date parseDate(String str) {
        if(str == null) return null;
        try {
            String formatStyle = "yyyy-MM-dd HH:mm:ss";

            if(str.length() == 10) {
                formatStyle = "yyyy-MM-dd";
            } else if(str.length() == 19) {
                formatStyle = "yyyy-MM-dd HH:mm:ss";
            } else if(str.length() == 13) {
                formatStyle = "yyyy-MM-dd HH";
            }
            return new SimpleDateFormat(formatStyle).parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断一个字符串日期是不是正确的格式
     *
     * @author lisheng
     * @date 2014年8月18日 下午2:47:43
     * @return boolean
     * @param dateStr
     * @param format
     */
    public static boolean checkDate(String dateStr, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (Exception e) {
            // 如果不能转换,肯定是错误格式
            return false;
        }
        String dateStr2 = df.format(date);
        // 转换后的日期再转换回String,如果不等,逻辑错误.如format为"yyyy-MM-dd",date为
        // "2006-02-31",转换为日期后再转换回字符串为"2006-03-03",说明格式虽然对,但日期逻辑上不对.
        return dateStr.equals(dateStr2);
    }

    /**
     * 获取两个日期间的小时和分钟数
     *
     * @author lisheng
     * @date 2015年7月24日 下午3:28:12
     * @return String
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getHoursAndMinutes(Date startDate, Date endDate) {
        if(null == startDate || null == endDate) {
            return "0";
        }

        long startTime = startDate.getTime();// 得出来的是毫秒数
        long endTime = endDate.getTime();// 得出来的是毫秒数

        if(endTime < startTime) {
            return "0";
        }

        long between = endTime - startTime;// 间隔毫秒数
        // long dayTime = between / (24 * 60 * 60 * 1000);
        long hourTime = between / (60 * 60 * 1000);// 间隔总小时数
        long minuteTime = (between / (60 * 1000)) - hourTime * 60;// 剩余分钟数

        String spendTime = String.valueOf(hourTime) + "小时" + formatTime(String.valueOf(minuteTime)) + "分钟";

        return spendTime;
    }

    /**
     * 获取两个日期间的小时数
     *
     * @author lisheng
     * @date 2015年7月28日 下午4:42:53
     * @return long
     * @param startDate
     * @param endDate
     */
    public static long getHours(Date startDate, Date endDate) {
        if(null == startDate || null == endDate) {
            return 0;
        }

        long startTime = startDate.getTime();// 得出来的是毫秒数
        long endTime = endDate.getTime();// 得出来的是毫秒数

        if(endTime < startTime) {
            return 0;
        }

        long between = endTime - startTime;// 间隔毫秒数
        long hoursTime = (between / (60 * 60 * 1000));

        return hoursTime;
    }

    /**
     * 获取两个日期间的分钟数
     *
     * @author lisheng
     * @date 2015年7月28日 下午4:42:53
     * @return long
     * @param startDate
     * @param endDate
     */
    public static long getMinutes(Date startDate, Date endDate) {
        if(null == startDate || null == endDate) {
            return 0;
        }

        long startTime = startDate.getTime();// 得出来的是毫秒数
        long endTime = endDate.getTime();// 得出来的是毫秒数

        if(endTime < startTime) {
            return 0;
        }

        long between = endTime - startTime;// 间隔毫秒数
        long minuteTime = (between / (60 * 1000));

        return minuteTime;
    }

    /**
     * 获取指定日期的第一天
     *
     * @author Nick
     * @date 2016年9月25日 下午12:33:41
     * @return String
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static String getFirstDay(String dateStr) throws Exception {
        Date date = null;
        String day_first = null;
        date = sdf_day.parse(dateStr);

        // 创建日历
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        day_first = sdf_day.format(calendar.getTime());
        return day_first;
    }

    /**
     * 获取指定日期的最后一天
     *
     * @author Nick
     * @date 2016年9月25日 下午12:34:28
     * @return String
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static String getLastDay(String dateStr) throws Exception {
        Date date = null;
        String day_last = null;
        date = sdf_day.parse(dateStr);

        // 创建日历
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1); // 加一个月
        calendar.set(Calendar.DATE, 1); // 设置为该月第一天
        calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
        day_last = sdf_day.format(calendar.getTime());
        return day_last;
    }

    /**
     * 获取过去或将来第几天的日期
     *
     * @param time
     *            (负数为过去，正数为将来)
     * @return yyyy-MM-dd
     */
    public static String getPastOrFetureDate(int time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + time);
        Date day = calendar.getTime();
        return sdf_day.format(day);
    }

    /**
     * 获取过去或将来多少分钟之后的时间
     * @param minutes
     * @return
     */
    public static String getPastOrFetureDateTime(int minutes) {
        Calendar calendar = Calendar.getInstance();
        // x分钟之前或之后的时间
        calendar.add(Calendar.MINUTE, minutes);
        Date day = calendar.getTime();
        return sdf.format(day);
    }


}

