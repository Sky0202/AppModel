package com.fanqie.appmodel.common.retrofit;

/**
 * 所有okhttp解码返回的数据
 * <p/>
 * Created by Administrator on 2016/7/29.
 */
public class HttpResult<T> {

    private int code;
    private String msg;
    private String time;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
