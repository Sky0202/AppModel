package com.fanqie.appmodel.common.utils;

import com.fanqie.appmodel.common.bean.RxBusBundle;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by Administrator on 2018/1/11.
 * Rxbus 工具类
 */

public class RxBus {

    private final FlowableProcessor<Object> mBus;

    private RxBus() {
        mBus = PublishProcessor.create().toSerialized();
    }

    private static class Holder {
        private static RxBus instance = new RxBus();
    }

    public static RxBus getInstance() {
        return Holder.instance;
    }

    // 发送消息
    public void post(@NonNull RxBusBundle bundle) {
        mBus.onNext(bundle);
    }

    // 注册
    public Flowable<RxBusBundle> register() {
        // 过滤操作
        return mBus.ofType(RxBusBundle.class);
    }

    // 解注册
    public void unregisterAll() {
        // 会将所有由 mBus 生成的 Flowable 都置为 completed 状态
        // 后续的 所有消息  都收不到了
        mBus.onComplete();
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }

}
