package com.fanqie.appmodel.common.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fanqie.appmodel.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 锁死返回键 activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements TakePhoto.TakeResultListener, InvokeListener {

    Dialog progressDialog;
    private Unbinder unbinder;

    /**
     * 创建时间：2017/11/9 16:50  描述：获取照片用到的实体
     */
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewId());
        unbinder = ButterKnife.bind(this);
        getTakePhoto().onCreate(savedInstanceState);
        //返回按钮
        onBack(setBackButton());
        //注册控制器
        registerPresenter();
        initView();
        iniIntent(getIntent());
        iniData();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 创建时间：2017/11/9 16:40  描述：获取 takePhoto 实例,供基类调用
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    /**
     * 创建时间：2017/11/9 16:49  描述：获取图片成功
     */
    @Override
    public void takeSuccess(TResult result) {

    }

    /**
     * 创建时间：2017/11/9 16:50  描述：获取图片失败
     */
    @Override
    public void takeFail(TResult result, String msg) {

    }

    /**
     * 创建时间：2017/11/9 16:50  描述：取消获取图片
     */
    @Override
    public void takeCancel() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    //设置xml文件
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
        progressDialog.dismiss();
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
