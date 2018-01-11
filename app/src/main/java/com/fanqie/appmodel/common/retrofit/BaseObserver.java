package com.fanqie.appmodel.common.retrofit;

import android.content.Context;

import com.fanqie.appmodel.common.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zpw on 2017/12/27.
 * 封装 观察者
 */

public abstract class BaseObserver<T> implements Observer<HttpResult<T>> {

    private Context mContext;

    public BaseObserver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HttpResult<T> httpResult) {
        if (httpResult.getCode() == 1) {
            onSuccess(httpResult.getData());
        } else {
            onFailure(httpResult.getCode(), httpResult.getMsg());
        }
    }


    @Override
    public void onError(Throwable e) {


    }

    @Override
    public void onComplete() {

    }

    protected void onFailure(int code, String msg) {
        if (code == 20003) {
            // 去登陆
            ToastUtils.showMessage("token 失效");
        } else {
            ToastUtils.showMessage(msg);
        }
    }

    protected abstract void onSuccess(T t);

}
