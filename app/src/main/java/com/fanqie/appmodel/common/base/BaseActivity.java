package com.fanqie.appmodel.common.base;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fanqie.appmodel.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 锁死返回键 activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Dialog progressDialog;
    private Unbinder unbinder;

    public View emptyView;

    private String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewId());

        emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null, false);
        unbinder = ButterKnife.bind(this);

        //返回按钮
        onBack(setBackButton());
        //注册控制器
        registerPresenter();
        initView();
        iniIntent(getIntent());
        iniData();

    }

    /**
     * 申请权限
     */
    public void requestPermission() {
        if (Build.VERSION.SDK_INT < 22) {
            return;
        }

        for (String permission : mPermissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BaseActivity.this, mPermissionList, 123);
                return;

            }
        }

    }

    //设置布局文件
    public abstract int setContentViewId();

    //设置返回按钮
    public abstract int setBackButton();

    //注册对应presenter
    public abstract void registerPresenter();

    //销毁对应的presenter
    public abstract void unRegisterPresenter();

    //初始化视图控件
    public abstract void initView();

    //用户的所有初始化操作
    public abstract void iniIntent(Intent it);

    //初始化数据
    public abstract void iniData();


    /**
     * 解除控制器
     */
    @Override
    protected void onDestroy() {
        unRegisterPresenter();
        unbinder.unbind();
        super.onDestroy();
    }

    public void showprogressDialog(String dialogMsg) {
        progressDialog = new Dialog(BaseActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setVisibility(dialogMsg.isEmpty() ? View.GONE : View.VISIBLE);
        msg.setText(dialogMsg);
        progressDialog.show();
    }

    public void showprogressTransparentDialog() {
        progressDialog = new Dialog(BaseActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog_transpare);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
    }

    //取消进度圈
    public void dismissProgressdialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    //返回键默认finish
    public void onBack(int resId) {
        if (resId != 0) {
            findViewById(resId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }


}
