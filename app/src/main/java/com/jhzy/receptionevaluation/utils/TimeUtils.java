package com.jhzy.receptionevaluation.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bigyu on 2017/3/1.
 * 时间工具类
 */

public class TimeUtils {


    /**
     * 获取当前年份
     *
     * @return
     */
    public static int year() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * @param date 日期
     * @return 转换为时间
     */
    public static String dateString(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = null;
        try {
            Date parse = format.parse(date);
            SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
            format1 = f.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format1;
    }

    public static String dateHours(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format1 = null;
        try {
            Date parse = format.parse(date);
            SimpleDateFormat f = new SimpleDateFormat("HH:mm");
            format1 = f.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format1;
    }

        public static String dateToYearMonth(String date) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String format1 = null;
            try {
                Date parse = format.parse(date);
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                format1 = f.format(parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return format1;
        }

}
