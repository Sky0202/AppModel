package com.fanqie.appmodel.common.utils;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 调试日志 工具类
 */

public final class DebugLog {

    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数

    private static final int JSON_INDENT = 2;

    private DebugLog() {
    }

    /* 调试日志的开关，一般Debug版本中打开，便于开发人员观察日志，Release版本中关闭 */
    public static final boolean ENABLED = true;

    /* 崩溃日志 */
    private static final String FILE_UNCAUGHT_EXCEPITON_LOG = "UncaughtException.log";

    /* 网络异常日志 */
    public static final String FILE_HTTP_EXCEPTION_LOG = "HttpException.log";

    //日志最大5M
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    /* TAG的前缀，便于过滤 */
    private static final String PREFIX = "zpw_";

    private static String createLog(String log) {

        StringBuilder builder = new StringBuilder();
        builder.append(methodName);
        builder.append("(").append(className).append(":").append(lineNumber).append(")*****");
        String formatLog = FormattingJson(log);
        builder.append(formatLog);
        return builder.toString();

    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static int v(String msg) {

        getMethodNames(new Throwable().getStackTrace());
        return ENABLED ? Log.v(PREFIX + className, "" + createLog(msg)) : 0;
    }

    public static int d(String msg) {

        getMethodNames(new Throwable().getStackTrace());

        // 华为的这款手机只能打印information信息
        if ("GEM-703L".equals(Build.MODEL)
                || "H60-L11".equals(Build.MODEL)) {
            return i(msg);
        }
        return ENABLED ? Log.d(PREFIX + className, createLog(msg)) : 0;
    }


    public static int i(String msg) {
        getMethodNames(new Throwable().getStackTrace());
        return ENABLED ? Log.i(PREFIX + className, createLog(msg)) : 0;
    }

    public static int w(String msg) {
        getMethodNames(new Throwable().getStackTrace());
        return ENABLED ? Log.w(PREFIX + className, createLog(msg)) : 0;
    }

    public static int e(String msg) {
        getMethodNames(new Throwable().getStackTrace());
        return ENABLED ? Log.e(PREFIX + className, createLog(msg)) : 0;
    }

    /**
     * 创建时间：2017/11/6 11:32  描述：格式化 json 数据
     */
    private static String FormattingJson(String json) {
        if (json == null || json.length() == 0) {
            return "Empty/Null json content";
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                return message;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                return message;
            }
            return "Invalid Json" + "\n" + json;
        } catch (JSONException e) {
            return "Invalid Json" + "\n" + json;
        }
    }

}
