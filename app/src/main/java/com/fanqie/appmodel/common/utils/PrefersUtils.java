package com.fanqie.appmodel.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fanqie.appmodel.common.application.MyApplication;

/**
 * 公共方法区首次设置和每次的读取放在闪屏页的地方
 * 默认使用包名做为缓存文件名
 * 使用之前在minfest里面注册application name
 */

public class PrefersUtils {

    //获取项目包名  get package name
    private static final String PACKAGE_NAME = MyApplication.getPackageInfo().packageName + ".config";

    private static final String PRE_NIGHT = "night";
    private static final String PRE_FIRST = "isFirst";
    private static final String PRE_SEND_INFO = "isSendInfo";
    private static final String PRE_SAVE_FLOW = "isSaveFlow";

    //获取以项目包名命名的sps文件 use packagename as sharepreference's file name
    public static SharedPreferences getSharedPreferences() {
        return MyApplication.getContext().getSharedPreferences(
                "PACKAGE_NAME", Context.MODE_PRIVATE);
    }

    /*****************************************************************************************
     * 常用方法区1 ：设置夜间模式
     ***************************************************************************************/
    public static void setNight() {
        getSharedPreferences().edit().putBoolean(PRE_NIGHT, true).commit();
    }

    public static void setDay() {
        getSharedPreferences().edit().putBoolean(PRE_NIGHT, false).commit();
    }

    public static void changeDay2Night() {
        boolean change = !getSharedPreferences().getBoolean(PRE_NIGHT, false);
        getSharedPreferences().edit().putBoolean(PRE_NIGHT, change).commit();
    }

    public static boolean isNight() {
        return getSharedPreferences().getBoolean(PRE_NIGHT, false);
    }

    /*****************************************************************************************
     * 常用方法区2 ：是否首次打开
     ***************************************************************************************/
    //首次获取默认是true
    public static boolean isFirstStart() {
        return getSharedPreferences().getBoolean(PRE_FIRST, true);
    }

    public static void setFirstStartToFalse() {
        getSharedPreferences().edit().putBoolean(PRE_FIRST, false).commit();
    }

    /*****************************************************************************************
     * 常用方法区3 ：是否省流量模式
     ***************************************************************************************/
    //首次获取默认是false
    public static boolean isSaveFlow() {
        return getSharedPreferences().getBoolean(PRE_SAVE_FLOW, false);
    }

    public static void setSaveFlowToTrue() {
        getSharedPreferences().edit().putBoolean(PRE_SAVE_FLOW, true).commit();
    }

    /*****************************************************************************************
     * 常用方法区3 ：是否开启消息推送
     ***************************************************************************************/
    //首次获取默认是true
    public static boolean isSendInfo() {
        return getSharedPreferences().getBoolean(PRE_SEND_INFO, true);
    }

    public static void setSendInfoToFalse() {
        getSharedPreferences().edit().putBoolean(PRE_SEND_INFO, false).commit();
    }

    /*****************************************************************************************
     * 公共方法区 String Boolean Long Int clear缓存
     ***************************************************************************************/
    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static Boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, 0L);
    }

    public static void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    public static void putBoolean(String key, Boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    public static void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    public static void putLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).commit();
    }

    // 清除所有缓存数据
    public static void clear() {
        getSharedPreferences().edit().clear().commit();
    }


}
