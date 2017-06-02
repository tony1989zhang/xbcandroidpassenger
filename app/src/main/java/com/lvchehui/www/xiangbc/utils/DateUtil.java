package com.lvchehui.www.xiangbc.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作用：时间，日期工具类
 */
public class DateUtil {
    public static final String TIME_FORMAT_YMD = "yyyy年MM月dd日";
    public static final String TIME_FORMAT_YMD2 = "yyyy-mm-dd";
    /**
     * 12小时制
     **/
    public static SimpleDateFormat DATA_FORMAT_hhMM = new SimpleDateFormat(
            "hh:mm");
    /**
     * 24小时制
     **/
    public static SimpleDateFormat DATA_FORMAT_HHMM = new SimpleDateFormat(
            "HH:mm");
    /* MM月dd日 */
    public static SimpleDateFormat DATA_FORMAT_MMDD = new SimpleDateFormat(
            "MM月dd日");
    /* yyyy年MM月dd日 */

    // 格式：年－月－日
    public static final String YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd";

    public static SimpleDateFormat DATA_FORMAT_YYYYMMDD = new SimpleDateFormat(
            "yyyy年MM月dd日");
    /* MM月dd日 HH:mm */
    public static SimpleDateFormat DATA_FORMAT_MMDD_HHMM = new SimpleDateFormat(
            "MM月dd日 HH:mm");
    /* yyyy年MM月dd日 HH:mm */
    public static SimpleDateFormat DATA_FORMAT_YYYYMMDD_HHMM = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm");
    /**
     * yyyy-MM-dd
     */
    public static SimpleDateFormat DATA_FORMAT_yyyy_MM_dd = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * 计算两个日期之间相差的天数
     */
    public static int calDaysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * string类型的转为Date  yyyy-MM-dd
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String dateToMonthDayDate(Date date){
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat("MM-dd");
        try {
            result = formater.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }

    public static String fetchMonthDay(String strDate){
        SimpleDateFormat formatter = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(strDate, pos);
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat("MM-dd");
        try {
            result = formater.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }

    public static boolean isToday(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Calendar calToday = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)
                && cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_YEAR) == calToday
                .get(Calendar.DAY_OF_YEAR)) {
            return true;
        }
        return false;
    }

    public static boolean isThisYear(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Calendar calToday = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    public static boolean isYesterday(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Calendar calToday = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)
                && cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_YEAR) == calToday
                .get(Calendar.DAY_OF_YEAR)) {
            return true;
        }
        return false;
    }

    public static int getDaysBetweenNow(long timeMillis) {
        Date date1 = new Date(timeMillis);
        Date dateNow = new Date(System.currentTimeMillis());
        return calDaysBetween(date1, dateNow);
    }

    public static String getTodayTimeString(long timeMillis) {
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMillis);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            sb.append("凌晨 ");
        } else if (hour >= 6 && hour < 12) {
            sb.append("上午 ");
        } else if (hour >= 12 && hour < 18) {
            sb.append("下午 ");
        } else {
            sb.append("晚上 ");
        }
        sb.append(DATA_FORMAT_hhMM.format(new Date(timeMillis)));
        return sb.toString();
    }

    public static boolean isDayDataNendUpdate(long lastMilliSeconds, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lastMilliSeconds);
        return isDayDataNendUpdate(cal, hour);
    }

    public static boolean isDayDataNendUpdate(Calendar cal, int hour) {
        Calendar calToday = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)
                && cal.get(Calendar.DAY_OF_YEAR) == calToday
                .get(Calendar.DAY_OF_YEAR)) {
            // 同一年同一天
            if (cal.get(Calendar.HOUR_OF_DAY) > hour
                    || calToday.get(Calendar.HOUR_OF_DAY) < hour)
                return false;
        }
        return true;
    }

    public static boolean isWeekDataNeedUpdate(long time, int dayofWeek,
                                               int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Calendar calToday = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)
                && cal.get(Calendar.WEEK_OF_YEAR) == calToday
                .get(Calendar.WEEK_OF_YEAR)) {
            if (cal.get(Calendar.DAY_OF_WEEK) <= dayofWeek
                    && cal.get(Calendar.HOUR_OF_DAY) < hour
                    && calToday.get(Calendar.DAY_OF_WEEK) >= dayofWeek
                    && calToday.get(Calendar.HOUR_OF_DAY) >= hour) {
                return true;
            }
            return false;
        }
        return true;
    }

    public static boolean isMonthDataNeedUpdate(long time, int dayOfMonth,
                                                int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Calendar calToday = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)
                && cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) {
            if (cal.get(Calendar.DAY_OF_MONTH) <= dayOfMonth
                    && cal.get(Calendar.HOUR_OF_DAY) < hour
                    && calToday.get(Calendar.DAY_OF_MONTH) >= dayOfMonth
                    && calToday.get(Calendar.HOUR_OF_DAY) >= hour) {
                return true;
            }
            return false;
        }
        return true;
    }

    public static String getConversationDate(long timeMillis) {
        StringBuilder sb = new StringBuilder();
        if (isThisYear(timeMillis)) {
            if (isToday(timeMillis)) {
                sb.append(getTodayTimeString(timeMillis));
            } else if (isYesterday(timeMillis)) {
                sb.append("昨天 ");
                sb.append(getTodayTimeString(timeMillis));
            } else {
                sb.append(DATA_FORMAT_MMDD.format(new Date(timeMillis)));
            }
        } else {
            sb.append(DATA_FORMAT_YYYYMMDD.format(new Date(timeMillis)));
        }
        return sb.toString();
    }

    public static String getChatDate(long timeMillis) {

        StringBuilder sb = new StringBuilder();
        if (isThisYear(timeMillis)) {
            if (isToday(timeMillis)) {
                sb.append(getTodayTimeString(timeMillis));
            } else if (isYesterday(timeMillis)) {
                sb.append("昨天 ");
                sb.append(getTodayTimeString(timeMillis));
            } else {
                sb.append(DATA_FORMAT_MMDD_HHMM.format(new Date(timeMillis)));
            }
        } else {
            sb.append(DATA_FORMAT_YYYYMMDD_HHMM.format(new Date(timeMillis)));
        }
        return sb.toString();
    }

    // 时间戳时间转换 年-月-日
    public static String getStrTime(long cc_time) {

        return getStrTime(cc_time, false);
    }

    /**
     * 时间转字符串
     * 年月日 yyyy-MM-dd
     * 月日   MM-dd
     * 时分   HH:mm
     * 月日时分 MM-dd  HH:mm
     * */
    public static String getDateToStr(long theDate, String pattern){

        Date date = new Date(theDate);
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        String format = sf.format(date);
        return format;
    }
    // 时间戳时间转换 年-月-日 时 分
    @SuppressLint("SimpleDateFormat")
    public static String getStrTime(long cc_time, boolean hasItemPicker) {

        SimpleDateFormat format = null;
        if (hasItemPicker) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");//yyyy-MM-dd HH:mm
        }
        return format.format(new Date(cc_time * 1000));
    }

    public static long getLongTime(String strTime) {
        return getLongTime(strTime, true);
    }

    public static long getLongTime(String strTime, String pattern) {
        XgoLog.e("strTime:" + strTime);
        SimpleDateFormat format =  new SimpleDateFormat(pattern);

        Date date = null;
        try {
            date = format.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        XgoLog.e("Format To times:" + date.getTime());
        return date.getTime()/1000;
    }

    public static long getLongTime(String strTime, boolean timePickerGone) {
        XgoLog.e("strTime:" + strTime);
        SimpleDateFormat format = null;
        if (timePickerGone) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");//yyyy-MM-dd HH:mm
        }

        Date date = null;
        try {
            date = format.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        XgoLog.e("Format To times:" + date.getTime());
        return date.getTime();
    }


    /**
     * java 时间戳13位转10位
     */
    public static String getSubLongStr(String strTime) {

        if (strTime.length() == 9) {
            return strTime.substring(0, 9);
        }
        return strTime.substring(0, 10);
    }

    // 获取当前时间
    public static String refFormatNowDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }

    public static String refFormatDate(int theDay) {
        return refFormatDate(theDay, "");
    }

    public static String refFormatDate(int theDay, String formatStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, theDay);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        if (!StringUtils.isEmpty(formatStr)) {
            return month + formatStr + year;
        }
        return year + "年" + month + "月";
    }

    public static String getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "";
    }

}
