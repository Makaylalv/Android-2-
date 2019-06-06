package com.john.software.helpeachother.code.Util.CountTimerUtils;

import java.util.Date;

/**
 * Created by Administrator on 2018/2/1.
 */

public class TimeUtil {
        private final static long minute = 60 * 1000;// 1分钟
        private final static long hour = 60 * minute;// 1小时
        private final static long day = 24 * hour;// 1天
        private final static long month = 31 * day;// 月
        private final static long year = 12 * month;// 年

        /**
         * 返回文字描述的日期
         *
         * @param date
         * @return
         */
        public static String getTimeFormatText(Date date) {
            if (date == null) {
                return null;
            }
            long diff = new Date().getTime() - date.getTime();
            long r = 0;
            if (diff > year) {
                r = (diff / year);
                return r + "年前";
            }
            if (diff > month) {
                r = (diff / month);
                return r + "个月前";
            }
            if (diff > day) {
                r = (diff / day);
                return r + "天前";
            }
            if (diff > hour) {
                r = (diff / hour);
                return r + "小时前";
            }
            if (diff > minute) {
                r = (diff / minute);
                return r + "分钟前";
            }
                return "刚刚";
        }
    public static boolean getif(Date date) {
        if (date == null) {
            return false;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {

            return true;
        }
        if (diff > month) {
            return true;
        }
        if (diff > day) {
            return  true;
        }
        if (diff > hour) {
            r = (diff / hour);
            if(r>4){
                return true;
            }else
            return false;
        }
        if (diff > minute) {
            r = (diff / minute);
            return false;
        }
        return false;
    }


}
