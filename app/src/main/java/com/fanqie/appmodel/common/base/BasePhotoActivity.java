package com.fanqie.appmodel.common.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fanqie.appmodel.R;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 创建时间：2018/3/28 11:06
 * <p>
 * 描述： 用来启动相机以及相册使用的类
 *
 * @author zpw
 */

public abstract class BasePhotoActivity extends TakePhotoActivity {

    private Dialog progressDialog;
    private Unbinder unbinder;

    public View emptyView;

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

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
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
        progressDialog = new Dialog(BasePhotoActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setVisibility(dialogMsg.isEmpty() ? View.GONE : View.VISIBLE);
        msg.setText(dialogMsg);
        progressDialog.show();
    }

    public void showprogressTransparentDialog() {
        progressDialog = new Dialog(BasePhotoActivity.this, R.style.progress_dialog);
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
