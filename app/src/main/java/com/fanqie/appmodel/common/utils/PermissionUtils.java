package com.fanqie.appmodel.common.utils;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限工具类
 * <p>
 * 申请获取权限
 * premissionUtils = new PremissionUtils(MainActivity.this);
 * premissionUtils.registerPermissionListener(new PremissionUtils.IPermissionFinish() {
 *
 * @Override public void permissionSuccess() {
 * Log.i("log", "--success--");
 * }
 * });
 * String[] premission = { PremissionUtils.writeexternal , PremissionUtils.carmera};
 * premissionUtils.askPermission(premission, 100);
 * <p>
 * 取得权限处理
 * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * premissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * }
 * <p>
 * 常见问题
 * 点击不调取
 * 查看manifest里面是否添加了需要申请的权限
 * 高版本也需要添加危险权限
 * fragment里面需要做额外的处理
 * <p>
 * <p>
 * Created by Administrator on 2016/9/5.
 */
public class PermissionUtils {

    private AppCompatActivity rootActivity;
    public static int REQUEST_CODE_PERMISSION = 0x00099;
    public static String carmera = Manifest.permission.CAMERA;
    public static String call = Manifest.permission.CALL_PHONE;
    public static String phonestate = Manifest.permission.READ_PHONE_STATE;
    public static String writeexternal = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static String location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private IPermissionFinish iPermissionFinish;
    private Object subscriber;

    public PermissionUtils(Object subscriber, AppCompatActivity rootActivity) {
        this.subscriber = subscriber;
        this.rootActivity = rootActivity;
    }

    /**
     * 申请接口
     */
    public void registerPermissionListener(IPermissionFinish iPermissionFinish) {
        this.iPermissionFinish = iPermissionFinish;
    }

    /**
     * 检查是否需要申请权限
     */
    public boolean checkPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT < 22) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(rootActivity, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取需要申请的权限
     */
    public List<String> getNeedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(rootActivity, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(rootActivity, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 申请权限
     * 在需要申请权限的地方调用
     */
    public void askPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermission(permissions)) {
            if (iPermissionFinish != null) {
                iPermissionFinish.permissionSuccess();
            }
        } else {
            List<String> needPermissions = getNeedPermissions(permissions);
            if (subscriber instanceof AppCompatActivity) {
                ActivityCompat.requestPermissions(rootActivity, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
            } else if (subscriber instanceof Fragment) {
                Fragment fragment = (Fragment) subscriber;
                fragment.requestPermissions(needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
            }

        }
    }


    /**
     * 系统权限回调处理
     * 需要在系统onRequestPermissionsResult方法里面调用
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DebugLog.i("requestCode--" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                if (iPermissionFinish != null) {
                    iPermissionFinish.permissionSuccess();
                } else {
                    showTipsDialog();
                }
            } else {
                showTipsDialog();
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            DebugLog.i("grantResults--" + grantResults);
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(rootActivity)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + rootActivity.getPackageName()));
        rootActivity.startActivity(intent);
    }

    /**
     * 成功失败接口
     */
    public interface IPermissionFinish {
        void permissionSuccess();
    }

    /**
     * 销毁
     */
    public void unregisterRootActivityAndSubscriber() {
        if (subscriber != null) {
            subscriber = null;
        }
        if (rootActivity != null) {
            rootActivity = null;
        }
    }

}
