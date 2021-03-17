package com.jain.lib;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    private static final DateFormat DEFAULT_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getFormattedDate(String date, String oldFormat, String newFormat) {

        // August 12, 2010
        if (isValidString(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
            try {
                Date d = sdf.parse(date);
                sdf.applyPattern(newFormat);
                return sdf.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public static Date string2Date(String time, String format) {
        return string2Date(time, new SimpleDateFormat(format));
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为用户自定义</p>
     *
     * @param time 时间字符串
     * @param format 时间格式
     * @return Date类型
     */
    public static Date string2Date(String time, SimpleDateFormat format) {
        return new Date(string2Milliseconds(time, format));
    }


    public static boolean isNotValidString(String text) {
        return (text == null
                || text.trim().length() == 0
                || text.equalsIgnoreCase(""));
    }


    public static boolean isValidString(String text) {
        return !isNotValidString(text);
    }

    public static long getRelativeIntervalByNow(String time, ConstUtils.TimeUnit unit,
                                                SimpleDateFormat format) {
        return getRelativeIntervalTime(getCurTimeString(format), time, unit, format);
    }

    public static long getRelativeIntervalTime(String time0, String time1, ConstUtils.TimeUnit unit,
                                               SimpleDateFormat format) {
        return milliseconds2Unit(
                (string2Milliseconds(time1, format) - string2Milliseconds(time0, format)), unit);
    }



    public static String getCurTimeString() {
        //        System.out.println("Current Time: " + date2String(new Date()));
        return date2String(new Date());
    }
    public static String getCurTimeString(SimpleDateFormat format) {
        //        System.out.println("current time: " + date2String(new Date(), format));
        return date2String(new Date(), format);
    }

    public static String date2String(final Date date) {
        return date2String(date, DEFAULT_FORMAT);
    }
    public static String date2String(final Date date, final DateFormat format) {
        return format.format(date);
    }

    private static long milliseconds2Unit(long milliseconds, ConstUtils.TimeUnit unit) {
        switch (unit) {
            case MSEC:
                return milliseconds / ConstUtils.MSEC;
            case SEC:
                return milliseconds / ConstUtils.SEC;
            case MIN:
                return milliseconds / ConstUtils.MIN;
            case HOUR:
                return milliseconds / ConstUtils.HOUR;
            case DAY:
                return milliseconds / ConstUtils.DAY;
        }
        return -1;
    }

    public static long string2Milliseconds(String time, SimpleDateFormat format) {
        try {
            //            System.out.println("time in millisecond: "+format.parse(time).getTime());
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long getMinutes(String time) {
        int temp = 0;
        try {
            String[] h1 = time.split(":");
            int hour = Integer.parseInt(h1[0]);
            int minute = Integer.parseInt(h1[1]);
            temp = (minute) + (60 * hour);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    public static String getSelectedDate(int difference, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, difference);
        return sdf.format(calendar.getTime());
    }

}
