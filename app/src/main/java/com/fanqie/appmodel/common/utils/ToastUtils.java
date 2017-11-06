package com.fanqie.appmodel.common.utils;

import android.widget.Toast;

import com.fanqie.appmodel.common.application.MyApplication;

/**
 * 创建时间：2017/5/9 17:54  描述：toast 提示优化
 */

public class ToastUtils {

    private static Toast toast;

    public static void showMessage(String message) {

        if (null == toast) {
            toast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.cancel();
            toast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
