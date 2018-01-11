package com.fanqie.appmodel.common.bean;

import android.os.Bundle;

/**
 * eventbus传递数据
 *
 * Created by Administrator on 2016/7/11.
 */
public class RxBusBundle {

    private String key;
    private String values;
    private Bundle bundle;

    public RxBusBundle(String key, String values) {
        this.key = key;
        this.values = values;
    }

    public RxBusBundle(String key, Bundle bundle) {
        this.key = key;
        this.bundle = bundle;
    }

    public RxBusBundle(String key, String values, Bundle bundle) {
        this.key = key;
        this.values = values;
        this.bundle = bundle;
    }

    public RxBusBundle() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public String toString() {
        return "EventBusBundle{" +
                "key='" + key + '\'' +
                ", values='" + values + '\'' +
                ", bundle=" + bundle +
                '}';
    }
}
