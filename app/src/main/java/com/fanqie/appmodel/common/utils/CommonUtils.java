package com.fanqie.appmodel.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.fanqie.appmodel.R;
import com.fanqie.appmodel.common.application.MyApplication;

/**
 * 通用工具类
 * <p>
 * Created by Administrator on 2016/8/13.
 */
public class CommonUtils {

    //验证码计时器
    public static CountDownTimer showCountDownTimer(final TextView tv_time) {

        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setClickable(false);
                tv_time.setText(millisUntilFinished / 1000 + "秒后可重发");
                //获取按钮上的文字
                SpannableString spannableString = new SpannableString(tv_time.getText().toString());
                ForegroundColorSpan span = new ForegroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.color_gray));

                if (millisUntilFinished / 1000 >= 10) {
                    spannableString.setSpan(span, 2, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannableString.setSpan(span, 1, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                tv_time.setText(spannableString);
            }

            @Override
            public void onFinish() {
                tv_time.setClickable(true);
                tv_time.setText("重新获取验证码");
            }
        };
        return countDownTimer;

    }

    /**
     * 获取版本号
     */
    public static String getVersion(Context context) //获取版本号
    {
        try {
            PackageInfo pi = MyApplication.getContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0.0";
        }
    }

    /**
     * 获取版本编号
     */
    public static int getVersionCode(Context context) //获取版本编号
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断网络连接
     */
    public static boolean isNetworkConnected() {

        if (MyApplication.getContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MyApplication.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
