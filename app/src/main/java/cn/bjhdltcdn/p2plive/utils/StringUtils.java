package cn.bjhdltcdn.p2plive.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * Created by wenquan on 2015/9/16.
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str) || "null".equals(str) || "NULL".equals(str)) {
            return true;
        }
        //过滤空格
        return str.equals(" ") || str.trim().equals("") || str.equals("");
    }

    public static boolean isEmpty(CharSequence str) {
        if (TextUtils.isEmpty(str) || "null".equals(str) || "NULL".equals(str)) {
            return true;
        }
        //过滤空格
        return str.equals(" ");
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isEquals(String str1, String str2) {
        return !isEmpty(str1) && !isEmpty(str2) && str1.equals(str2);
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证密码是否符合要求，密码要求格式6-20位字母或数字、字符，全字母或全数字都可以
     */
    public static boolean isFormatedPassword(String psd) {
        Pattern p = compile("[^\\u3E00-\\u9FA5]{6,20}");
        Matcher m = p.matcher(psd);
        return m.matches();
    }

    /**
     * 验证邀请码，8位数字组或字母组合
     *
     * @param code
     * @return
     */
    public static boolean isFormatedCode(String code) {
        Pattern p = compile("[a-zA-Z0-9]{8}");
        Matcher m = p.matcher(code);
        return m.matches();
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证手机号是否符合要求，要求“135”“137”“151”“152”等有效字段开头的11位数字
     */
    public static boolean isFormatedPhoneNumber(String phonenumber) {
//        Pattern p = Pattern.compile("^(13[0-9]|15[0-9]|18[0,5-9])\\d{8}$");
        Pattern p = compile("^[1][3,4,5,7,8][0-9]{9}$");

        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证qq号是否符合要求，要求全是数字 长度从5位到10位
     */
    public static boolean isFormatedQq(String phonenumber) {
        Pattern p = compile("^[1-9]\\d{4,10}$");
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证微信号是否符合要求，要求
     */
    public static boolean isFormatedWx(String phonenumber) {
        Pattern p = compile("^[a-zA-Z\\d_]{5,20}$");
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证默默号是否符合要求，要求
     */
    public static boolean isFormatedMM(String phonenumber) {
        Pattern p = compile("^[0-9]{5,20}$");
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证姓名是否符合要求，要求只能是汉字或英文字符，最长20个字符
     */
    public static boolean isFormated(String phonenumber) {
        Pattern p = compile("^[\\u3E00-\\u9FA5a-zA-Z_0-9]{4,20}$");
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证姓名是否符合要求，要求只能是汉字或英文字符，最长15个字符
     */
    public static boolean isFormatedName(String phonenumber) {
        Pattern p = compile("^[\\u3E00-\\u9FA5a-zA-Z_0-9]{0,15}$");
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证姓名是否符合要求，要求只能是汉字或英文字符，最长30个字符
     */
    public static boolean isFormatedd(String phonenumber) {
        Pattern p = compile("^[\\u3E00-\\u9FA5a-zA-Z_0-9]{0,30}$");
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    /**
     * 匹配视频房间密码
     *
     * @param password
     * @return
     */
    public static boolean isFormatedGroupVideoRoomPassword(String password) {
        Pattern p = compile("^[a-zA-Z_0-9]{6,10}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 匹配视频房间口令
     *
     * @param command 房间口令
     * @return
     */
    public static boolean isFormatedGroupVideoRoomCommand(String command) {
        Pattern p = compile("^[\\u3E00-\\u9FA5a-zA-Z_0-9]{8,20}$");
        Matcher m = p.matcher(command);
        return m.matches();
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证姓名是否符合要求，要求只能是汉字或英文字符，最长5个字符
     */
    public static boolean isFormatedTag(String tag) {
        Pattern p = compile("^[\\u3E00-\\u9FA5a-zA-Z_]{0,5}$");
        Matcher m = p.matcher(tag);
        return m.matches();
    }

    /**
     * 把字符串的某几位替换成其他字符
     *
     * @param str
     * @param start 开始位
     * @param end   结束位
     * @param s     替换成的标志字符
     * @return
     */

    public static String replaceSubString(String str, int start, int end, String s) {
        String subLeft = "";
        String subRight = "";
        try {
            subLeft = str.substring(0, start);
            subRight = str.substring(end, str.length());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < end - start; i++) {
                sb = sb.append("*");
            }
            subLeft += sb.toString();
            subLeft += subRight;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return subLeft;
    }

    /**
     * 去除字符串中的所有空格
     *
     * @param str
     * @return
     */
    public static String removeAllSpace(String str) {
        String tmpstr = str.replace(" ", "");
        return tmpstr;
    }

    /**
     * @prama: str 要判断是否包含特殊字符的目标字符串
     */

    public static boolean compileExChar(String str) {

        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Pattern pattern = compile(limitEx);
        Matcher m = pattern.matcher(str);
        return m.find();


    }


    /***
     * 用^_^替换键盘输入的表情符
     *
     * @param source
     * @return
     */
    public static CharSequence emojiFilte(CharSequence source) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }
        Pattern emoji = compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]", UNICODE_CASE | CASE_INSENSITIVE);
        Matcher matcher = emoji.matcher(source);

        String replaceResult = matcher.replaceAll("");
        Logger.v("", "表情过滤后：" + replaceResult);
        return replaceResult;
    }

    /**
     * 替换掉所有特殊字符和空格
     */
    public static String stringFilter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& ;*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");

    }

    /**
     * 匹配所有特殊字符和空格
     */
    public static boolean stringFilter2(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& ;*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();

    }

    /**
     * 匹配中文和英文数字
     *
     * @return
     */
    public static boolean patternTag(String str) {
//        Pattern p = Pattern.compile("^[\\u3E00-\\u9FA5a-zA-Z0-9]{0,5}$");
        Pattern p = compile("^[\\u3E00-\\u9FA5a-zA-Z0-9]{0,}$");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 去掉手机号好的特殊字符 如：+86  86开始的  带有空格 或- 的手机号码
     *
     * @param contactPhone
     * @return
     */
    public static String convertPhoneNum(String contactPhone) {
        if (contactPhone.startsWith("+86")) {
            contactPhone = contactPhone.replace("+86", "");
        } else if (contactPhone.startsWith("86")) {
            contactPhone = contactPhone.replace("86", "");
        } else if (contactPhone.contains("-")) {
            contactPhone = contactPhone.replace("-", "");
        } else if (contactPhone.contains(" ")) {
            contactPhone = contactPhone.replace(" ", "");
        }
        return contactPhone;
    }

    /**
     * @author huwh
     * @date 2015/9/21
     * @function 验证是否是纯数字
     */
    public static boolean isFormatedNumber(String phonenumber) {
        Pattern p = compile("^[1-9]\\d{1,15}$");

        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }


    /**
     * 截取用户个人信息地址的城市
     *
     * @param location
     * @return
     */
    public static String sqliteLocation(String location) {
        try {
            String city = location;
            if (!StringUtils.isEmpty(location)) {
                //截取省
                int provinceIndex = location.indexOf(" ");
                if (provinceIndex > 0) {
                    city = location.substring(0, provinceIndex);
                    if (!city.equals("北京市") && !city.equals("天津市") && !city.equals("上海市") && !city.equals("重庆市")) {
                        //除去直辖市之后才截取市
                        int cityIndex = location.indexOf(" ");
                        int areaIndex = location.lastIndexOf(" ");
                        if (cityIndex != -1 && areaIndex != -1) {
                            city = location.substring(cityIndex + 1, areaIndex);
                        }
                    }

                }
                return city;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "火星";
    }

    /**
     * 匹配邮箱
     */
    public static boolean isEmail(String str) {
        String regEx = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern p = compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证身份证
     */
    public static boolean IDCardValidate(String IDStr) {
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度18位 ================
        if (IDStr.length() != 18) {
            return false;
        }
        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        }
        if (isNumeric(Ai) == false) {
            //errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 日
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
//          errorInfo = "身份证生日无效。";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                //errorInfo = "身份证生日不在有效范围。";
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            //errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            //errorInfo = "身份证日期无效";
            return false;
        }
        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            //errorInfo = "身份证地区编码错误。";
            return false;
        }
        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                //errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings("unchecked")
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
//      hashtable.put("71", "台湾");
//      hashtable.put("81", "香港");
//      hashtable.put("82", "澳门");
//      hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern =
                compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @return 结果SpannableString
     */
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        if (text.contains(keyword) && !TextUtils.isEmpty(keyword)) {
            try {
                Pattern p = Pattern.compile(keyword);
                Matcher m = p.matcher(s);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (Exception e) {
            }
        }
        return s;
    }
}
