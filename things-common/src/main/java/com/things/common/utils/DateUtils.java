package com.things.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endTime   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取当天开始时间
     *
     * @return 当天开始时间
     */
    public static Date getNowDateStartTime() {
        //获取当前时间
        LocalDateTime nowTime = LocalDateTime.now();
        //获取当前日期
        LocalDate nowDate = LocalDate.now();
        //设置零点
        LocalDateTime beginTime = LocalDateTime.of(nowDate, LocalTime.MIN);
        //将时间进行格式化
        return Date.from(beginTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getDateStartTime(LocalDate nowDate) {
        //设置零点
        LocalDateTime beginTime = LocalDateTime.of(nowDate, LocalTime.MIN);
        //将时间进行格式化
        return Date.from(beginTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当天结束时间
     *
     * @return 当天结束时间
     */
    public static Date getNowDateEndTime() {
        //获取当前时间
        LocalDateTime nowTime = LocalDateTime.now();
        //获取当前日期
        LocalDate nowDate = LocalDate.now();
        //设置当天的结束时间
        LocalDateTime endTime = LocalDateTime.of(nowDate, LocalTime.MAX);
        //将时间进行格式化
        return Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取某天结束时间
     *
     * @param nowDate 某天
     * @return 某天结束时间
     */
    public static Date getDateEndTime(LocalDate nowDate) {
        //设置当天的结束时间
        LocalDateTime endTime = LocalDateTime.of(nowDate, LocalTime.MAX);
        //将时间进行格式化
        return Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当月第一天
     *
     * @return 某月第一天
     */
    public static Date firstDayOfMonth() {
        LocalDate now = LocalDate.now();
        LocalDateTime localDateTime = now.atStartOfDay();
        LocalDateTime firstDayOfMonth = localDateTime.withDayOfMonth(1);
        //将时间进行格式化
        return Date.from(firstDayOfMonth.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取某月第一天
     *
     * @param date 某月
     * @return 某月第一天
     */
    public static Date getFirstDayOfMonth(LocalDate date) {
        LocalDate first = date.with(TemporalAdjusters.firstDayOfMonth());
        //获取一天的第一秒和最后一秒
        LocalTime beginTime = LocalTime.MIN;
        //某月的第一天的第一秒
        LocalDateTime beginDatetime = LocalDateTime.of(first, beginTime);

        return Date.from(beginDatetime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取某月最后一天
     *
     * @param date 某月
     * @return 某月最后一天
     */
    public static Date getLastDayOfMonth(LocalDate date) {
        LocalDate last = date.with(TemporalAdjusters.lastDayOfMonth());
        //获取一天的第一秒和最后一秒
        LocalTime endTime = LocalTime.MAX;
        //某月的最后一天的最后一秒
        LocalDateTime endDatetime = LocalDateTime.of(last, endTime);
        return Date.from(endDatetime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当月最后一天
     *
     * @return 当月最后一天
     */
    public static Date lastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date time = cal.getTime();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(time) + " 23:59:59";
        return parseDate(date);
    }

    /**
     * 获取两个时间内的每一天
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 天集合
     */
    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= numOfDaysBetween; i++) {
            LocalDate date = startDate.plusDays(i);
            dates.add(date);
        }
        return dates;
    }

    /**
     * 获取两个时间内的每个月
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 月集合
     */
    public static List<LocalDate> getMonthBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        long numOfDaysBetween = ChronoUnit.MONTHS.between(startDate, endDate);
        for (int i = 0; i <= numOfDaysBetween; i++) {
            LocalDate date = startDate.plusMonths(i);
            dates.add(date);
        }
        return dates;
    }

}
