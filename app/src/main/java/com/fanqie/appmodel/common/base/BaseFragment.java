package com.fanqie.appmodel.common.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanqie.appmodel.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * fragment抽象基类
 * <p/>
 * Created by Administrator on 2016/6/2.
 */
public abstract class BaseFragment extends Fragment {

    private static final long DURATION = 200;
    Dialog progressDialog;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //判断是否有布局文件
        View view = null;
        if (setContentViewId() != 0) {
            view = inflater.inflate(setContentViewId(), container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        iniData();
        return view;
    }

    //进度圈
    public void showprogressDialog(FragmentActivity baseActivity, String dialogMsg) {
        progressDialog = new Dialog(baseActivity, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setVisibility(dialogMsg.isEmpty() ? View.GONE : View.VISIBLE);
        msg.setText(dialogMsg);
        progressDialog.show();
    }

    public void showprogressTransparentDialog(FragmentActivity baseActivity) {
        progressDialog = new Dialog(baseActivity, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog_transpare);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
    }

    //取消进度圈
    public void dismissProgressdialog() {
        progressDialog.dismiss();
    }

    //初始化视图
    public abstract int setContentViewId();

    //初始化控件
    public abstract void initView(View view);

    //初始化数据
    public abstract void iniData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
