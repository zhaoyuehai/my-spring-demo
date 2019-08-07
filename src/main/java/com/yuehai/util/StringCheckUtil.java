package com.yuehai.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaoyuehai 2019/8/7
 */
public class StringCheckUtil {
    /**
     * 2-20位
     */
    public static boolean isNickName(String userName) {
        if (userName == null || "".equals(userName)) return false;
        String regex = "^[\\u4E00-\\u9FA5A-Za-z0-9]{2,20}$";
        return userName.matches(regex);
    } /**
     * 4-16位字母、数字、下划线
     */
    public static boolean isUserName(String userName) {
        if (userName == null || "".equals(userName)) return false;
        String regex = "^[a-zA-Z0-9_-]{4,16}$";
        return userName.matches(regex);
    }

    /**
     * 6-16位数字或字母
     */
    public static boolean isPassword(String password) {
        if (password == null || "".equals(password)) return false;
        String regex = "^[0-9a-zA-Z]{6,16}$";
        return password.matches(regex);
    }

    public static boolean isEmail(String email) {
        if (email == null || "".equals(email)) return false;
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
