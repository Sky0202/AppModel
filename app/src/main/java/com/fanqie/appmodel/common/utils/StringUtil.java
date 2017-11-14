package com.fanqie.appmodel.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 文字工具类封装
 * Created by Administrator on 2016/6/20.
 */
public class StringUtil {

    /*************************************************************************
     * 公共方法区
     *************************************************************************/

    //用户名校验
    public static boolean isStringCharacterFilter(String str) {
        String regEx = "[0-9[a-zA-Z]] ";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String result = m.replaceAll("").trim();
        if (result.equals(str)) {
            return true;
        }
        return false;
    }

    //文字长度校验
    public static boolean StringLength(int minLength, int maxLength, String str) {
        if (str.length() > maxLength && str.length() < minLength) {
            return false;
        }
        return true;
    }

    //手机号11位验证+正则表达式验证+空判断
    // 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    // 联通：130、131、132、152、155、156、185、186
    // 电信：133、153、180、189、（1349卫通）
    // 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    public static boolean phoneNumVerification(String phoneNum) {
        String telRegex = "[1][123456789]\\d{9}";
        if (!phoneNum.isEmpty()) {
            if (phoneNum.length() == 11) {//判断位数
                if (phoneNum.matches(telRegex)) {//判断正则表达式
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String cutString(String str) {
        String substring = "";
        if (str.length() > 0) {
            substring = str.substring(0, str.length() - 1);
            return substring;
        }
        return substring;
    }

    public static String cutImageEditString(String str) {
        String substring = "";
        if (str.length() > 0) {
            substring = str.substring(1, str.length() - 4);
            return substring;
        }
        return substring;
    }

    //判断填写的内容是不是数字
    public static boolean judgeNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 截取字符串
     */
    public static String cubString(String str, int start, int end) {
        return str.substring(start, end);
    }

    /**
     * json文件读取
     */
    public static String readText(Context context, String assetPath) {
        LogUtils.debug("read assets file as text: " + assetPath);
        try {
            return ConvertUtils.toString(context.getAssets().open(assetPath));
        } catch (Exception e) {
            LogUtils.error(e);
            return "";
        }
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建时间：2017/4/1 11:02  描述：设置字符串颜色
     */
    public static SpannableStringBuilder setTextColor(int start, String text) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(Color.GRAY), start, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * 登录验证
     */
    public static boolean verifyLogin(String name, String pwd) {

        if (name.isEmpty()) {
            ToastUtils.showMessage("手机号不能为空");
            return false;
        } else if (!phoneNumVerification(name)) {
            ToastUtils.showMessage("手机号输入有误");
            return false;
        } else if (pwd.isEmpty()) {
            ToastUtils.showMessage("密码不能为空");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 注册验证
     */
    public static boolean verifyNumber(String name, String code, String pwd, String confPwd) {

        if (name.isEmpty()) {
            ToastUtils.showMessage("手机号不能为空");
            return false;
        } else if (!phoneNumVerification(name)) {
            ToastUtils.showMessage("手机号输入有误");
            return false;
        } else if (code.isEmpty()) {
            ToastUtils.showMessage("验证码不能为空");
            return false;
        } else if (pwd.isEmpty() || confPwd.isEmpty()) {
            ToastUtils.showMessage("密码不能为空");
            return false;
        } else if (pwd.length() < 6 || confPwd.length() < 6) {
            ToastUtils.showMessage("请至少设置6位密码");
            return false;
        } else if (!pwd.equals(confPwd)) {
            ToastUtils.showMessage("两次输入密码不一致");
            return false;
        } else if (!pwdVertify(confPwd)) {
            ToastUtils.showMessage("密码格式有误");
            return false;
        } else {
            return true;
        }
    }

    // 校验密码   不包含特殊字符, 最少6位
    public static boolean pwdVertify(String pwd) {
        String pwdRegex = "^[a-zA-Z0-9]{6,16}$";
        if (!pwd.isEmpty()) {
            if (pwd.matches(pwdRegex)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
