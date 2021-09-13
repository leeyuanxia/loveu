package com.zcgc.loveu.utils;

import java.util.Calendar;
import java.util.TimeZone;

public class TimeUtils {

    public static long getTimeFromDateString(String dateString){
        String[] times = dateString.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); //  这里时区需要设置一下，不然会有8个小时的时间差
        calendar.set(Calendar.YEAR, Integer.parseInt(times[0]));//
        calendar.set(Calendar.MONTH, Integer.parseInt(times[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(times[2]));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTimeInMillis();
    }
    public static long getTimeFromHAMString(String HAMString){
        String[] times = HAMString.split(":");
        return Long.parseLong(times[0])*60*60*1000+Long.parseLong(times[1])*60*1000;
    }

    public static int calDayDistanceFromNow(long time) {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.set(Calendar.HOUR_OF_DAY,0);
        calendarNow.set(Calendar.MINUTE,0);
        calendarNow.set(Calendar.SECOND,0);
        Calendar calendarThatTime = Calendar.getInstance();
        calendarThatTime.setTimeInMillis(time);
        calendarThatTime.set(Calendar.HOUR_OF_DAY,0);
        calendarThatTime.set(Calendar.MINUTE,0);
        calendarThatTime.set(Calendar.SECOND,0);
        return (int) ((calendarThatTime.getTimeInMillis()-calendarNow.getTimeInMillis())/(24*60*60*1000));
    }
}
