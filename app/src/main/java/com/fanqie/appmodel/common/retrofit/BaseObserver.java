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

    private final int NO_TOKEN = 20003;

    public BaseObserver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HttpResult<T> httpResult) {
        int code = httpResult.getCode();
        if (code == 1 || code == 0) {
            onSuccess(httpResult.getData());
        } else if (code == NO_TOKEN) {
            // 去登陆
            ToastUtils.showMessage("登录信息过期");
        } else {
            ToastUtils.showMessage(httpResult.getMsg());
            onFailure();
        }
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(T t);

    protected abstract void onFailure();

    @Override
    public abstract void onError(Throwable e);

}
