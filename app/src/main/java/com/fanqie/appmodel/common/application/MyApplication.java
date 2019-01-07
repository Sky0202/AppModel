package com.fanqie.appmodel.common.application;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .   ' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * <p>
 * .............................................
 * 佛祖保佑             永无BUG
 */


public class MyApplication extends Application {

    private static Context mApplicationContext;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        mApplicationContext = this;
        instance = this;
        super.onCreate();
    }

    /**
     * 获取ApplicationContext
     */
    public static Context getContext() {
        return mApplicationContext;
    }

    /**
     * 获取application
     */
    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 获取本程序包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo() {
        try {
            PackageInfo pinfo = mApplicationContext.getPackageManager()
                    .getPackageInfo(mApplicationContext.getPackageName(), 0);
            return pinfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
