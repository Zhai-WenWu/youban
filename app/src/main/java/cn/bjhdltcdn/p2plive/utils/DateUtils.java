package cn.bjhdltcdn.p2plive.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wenquan on 2016/6/23.
 */
public class DateUtils {
    public static String DATE_FORMAT_1 = "yyyy-MM-dd";
    /**
     * 指定日期格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_3 = "HH:mm";
    public static final String DATE_FORMAT_4 = "yy.MM.dd";
    public static final String DATE_FORMAT_5 = "yy-MM-dd";
    public static final String DATE_FORMAT_6 = "MM-dd HH:mm";
    public static final String DATE_FORMAT_7 = "yyyy年MM月dd日 HH:mm";
    public static final String DATE_FORMAT_9 = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_8 = "yyyy-MM-dd HH";
    public static final String DATE_FORMAT_10 = "yyyy-MM-dd";

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_1, Locale.CHINA);
        return format.format(date);
    }

    public static String getFormatDataString(long timeMillis) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_2, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String getFormatDataString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 把字符串时间转换成date类型
     *
     * @param strDate
     * @return
     */
    public static Date getFromatDate(String strDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_2);
            Date date = null;
            try {
                date = sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getFromatDate(String strDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }


    public static boolean isToday(Date workDay) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Date currTime = new Date();

        c1.setTime(workDay);
        c2.setTime(currTime);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isTomorrow(Date workDay) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();


        Date currTime = new Date();

        c1.setTime(workDay);
        c2.setTime(currTime);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && day1 == (day2 + 1);
    }

    /**
     * 判断哪天
     *
     * @param strDate
     * @return
     */
    public static String getDay(String strDate) {
        Date getDate = DateUtils.getFromatDate(strDate);
        String day = "";
        if (DateUtils.isToday(getDate)) {
            day = "今天";
        } else if (DateUtils.isTomorrow(getDate)) {
            day = "明天";
        } else {
            day = strDate.substring(0, strDate.indexOf(" ") + 1);
        }
        return day;
    }


    /**
     * 获取当前年份
     *
     * @return
     */
    public static String getYearTime() {
        Calendar calendar = Calendar.getInstance();
        String yearTime = calendar.get(Calendar.YEAR) + "";
        return yearTime;
    }

    /**
     * 今天，显示 今天 + 时间
     * 今年内，今天以前，显示月日，如12-03
     * 去年以前的，显示年份后两个数 14-03-22
     *
     * @return
     */
    public static String covertToSpellListTime(String strDate) {
        try {
            Date getDate = getFromatDate(strDate);
            if (isToday(getDate)) {
                String hourmims = strDate.substring(strDate.indexOf(" ") + 1, strDate.lastIndexOf(":"));
                //            return "今天" + hourmims;
                return hourmims;
            } else if (isTomorrow(getDate)) {
                String hourmims = strDate.substring(strDate.indexOf(" ") + 1, strDate.lastIndexOf(":"));
                return "明天" + hourmims;
                //            try {
                //                if (strDate.contains(DateUtils.getYearTime())) { // 是否是同一年
                //                    String addTime = DateUtils.getFormatDataString(DateUtils.getFromatDate(strDate, DateUtils.DATE_FORMAT_2),DateUtils.DATE_FORMAT_6);
                //                    return addTime;
                //                } else {
                //                    String addTime = DateUtils.getFormatDataString(DateUtils.getFromatDate(strDate,DateUtils.DATE_FORMAT_2),DateUtils.DATE_FORMAT_5);
                //                    return addTime;
                //                }
                //            } catch (Exception e) {
                //                e.printStackTrace();
                //            }

            } else {
                return strDate;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 今天，显示 今天 + 时间
     * 今年内，今天以前，显示月日+时间，如12-03 00:00
     * 去年以前的，显示年份后两个数 14-03-22
     *
     * @return
     */
    public static String covertToShowTime(String strDate) {
        Date getDate = getFromatDate(strDate);
        if (isToday(getDate)) {
            String hourmims = strDate.substring(strDate.indexOf(" "), strDate.lastIndexOf(":"));
//            return "今天" + hourmims;
            return hourmims;
        } else {

            try {
                if (strDate.contains(DateUtils.getYearTime())) { // 是否是同一年
                    String addTime = DateUtils.getFormatDataString(DateUtils.getFromatDate(strDate, DateUtils.DATE_FORMAT_2), DateUtils.DATE_FORMAT_6);
                    return addTime;
                } else {
                    String addTime = DateUtils.getFormatDataString(DateUtils.getFromatDate(strDate, DateUtils.DATE_FORMAT_2), DateUtils.DATE_FORMAT_5);
                    return addTime;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return strDate;
    }

    /**
     * 显示 YY-MM-dd HH:mm
     *
     * @param strDate
     * @return
     */
    public static String showYYYYMMddHHmm(String strDate) {
        return DateUtils.getFormatDataString(DateUtils.getFromatDate(strDate, DateUtils.DATE_FORMAT_2), DateUtils.DATE_FORMAT_2);
    }

    /**
     * 显示 MM-dd HH:mm
     *
     * @param strDate
     * @return
     */
    public static String showMMddHHmm(String strDate) {
        return DateUtils.getFormatDataString(DateUtils.getFromatDate(strDate, DateUtils.DATE_FORMAT_2), DateUtils.DATE_FORMAT_6);
    }

    /**
     * 显示HH:mm
     *
     * @param strDate
     * @return
     */
    public static String showHHmm(String strDate) {
        return DateUtils.getFormatDataString(DateUtils.getFromatDate(strDate, DateUtils.DATE_FORMAT_2), DateUtils.DATE_FORMAT_3);
    }

    /**
     * 今天，显示 今天
     * 今年内，今天以前，显示月日，如12-03
     * 去年以前的，显示年份后两个数 14-03-22
     *
     * @return
     */
    public static String covertToShowDate(String strDate) {
        Date getDate = getFromatDate(strDate, DATE_FORMAT_1);
        if (isToday(getDate)) {
            return "今天";
        } else if (isTomorrow(getDate)) {
            return "明天";
        } else {
            return getTime(getDate);
        }
    }


    /**
     * 获取 GMT 格式时间戳
     *
     * @return GMT 格式时间戳
     */
    public static String getGMTDate() {
        SimpleDateFormat formater = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        formater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formater.format(new Date());
    }

    /**
     * 获取年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

}
