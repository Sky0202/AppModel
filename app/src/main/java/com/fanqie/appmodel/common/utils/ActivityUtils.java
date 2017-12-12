package com.fanqie.appmodel.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.fanqie.appmodel.common.constants.ConstantString;


/**
 * Created by zpw on 2017/5/18.
 * <p>
 * activity 工具类
 */

public class ActivityUtils {

    /**
     * 创建时间：2017/5/18 11:51  描述：页面跳转 不带参数
     */
    public static void startActivity(Context context, Class targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }

    /**
     * 创建时间：2017/5/18 11:51  描述：页面跳转 带参数
     */
    public static void startActivityWithArg(Context context, Class targetActivity, String arg) {
        Intent intent = new Intent(context, targetActivity);
        intent.putExtra("arg", arg);
        context.startActivity(intent);
    }

    /**
     * 创建时间：2017/5/18 11:51  描述：页面跳转 带参数
     */
    public static void startActivityWithInt(Context context, Class targetActivity, int from) {
        Intent intent = new Intent(context, targetActivity);
        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    /**
     * 创建时间：2017/5/18 11:51  描述：页面跳转 带参数
     */
    public static void startActivityWithBundle(Context context, Class targetActivity, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    /**
     * 创建时间：2017/5/24 14:50  描述：拨打客服电话
     */
    public static void callKefu(Context context) {

//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ConstantString.IS_LOGIN));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

    }


}
