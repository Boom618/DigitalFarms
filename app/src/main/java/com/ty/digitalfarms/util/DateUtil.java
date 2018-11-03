package com.ty.digitalfarms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/1.
 */

public class DateUtil {
    /**
     * date2比date1多的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int differentDays(Date start, Date end) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(start);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    public static Date getDateFromStr(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }

    /**
     * 根据当前日期和日期间隔获取日期
     *
     * @param day 日期间隔
     * @return 日期字符串
     */
    public static String getStateTime(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * 根据起始日期和间隔时间计算结束日期
     *
     * @param sDate 开始时间
     * @param days  间隔时间
     * @return 结束时间
     */
    public static Date getDalculateEndDate(Date sDate, int days) {
        //将开始时间赋给日历实例
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setTime(sDate);
        //计算结束时间
        sCalendar.add(Calendar.DATE, days);
        //返回Date类型结束时间
        return sCalendar.getTime();
    }

    /**
     *
     * @param day
     * @param calendar
     * @param x
     * @return
     */
    public static String addTime(String day,int calendar, int x) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
        // 引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变
        // 量day格式一致
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        System.out.println("front:" + format.format(date)); // 显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendar, x);// 24小时制
        date = cal.getTime();
        System.out.println("after:" + format.format(date)); // 显示更新后的日期
        cal = null;
        return format.format(date);

    }
}
