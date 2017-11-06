package com.fanqie.appmodel.common.bean;

/**
 * 所有okhttp解码返回的数据
 * <p/>
 * Created by Administrator on 2016/7/29.
 */
public class JsonResult {

    private String code;
    private String msg;
    private String time;
    private String data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JsonResult(String Result, String ResultDesc, String Data) {
        this.code = Result;
        this.msg = ResultDesc;
        this.data = Data;
    }


    public JsonResult() {
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

}
