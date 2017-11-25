package com.fanqie.appmodel.common.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.fanqie.appmodel.common.application.MyApplication;
import com.fanqie.appmodel.common.bean.JsonResult;
import com.fanqie.appmodel.common.constants.ConstantString;
import com.fanqie.appmodel.main.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * okhttp封装方法
 * post异步
 * get异步
 * <p/>
 * <p/>
 * 200 正确
 * 500 服务器错误
 * 404 无响应（一般没有）
 * <p/>
 * result 0无数据 1数据正确 -1返回错误
 * data json串
 * msg 返回信息
 * page 页数（基本没啥用）
 * <p/>
 * 不需要用户令牌
 * <p/>
 * Created by Administrator on 2016/5/31.
 */
public class OkhttpUtils {

    private static OkhttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private static final int SECOND = 20; //各种超时的时间限制
    private Handler mDelivery;

    private static final int JSON_INDENT = 2;

    private OkhttpUtils() {
        mOkHttpClient = new OkHttpClient();
        //设置连接超时请求超时
        mOkHttpClient.newBuilder().connectTimeout(SECOND, TimeUnit.SECONDS);
        mOkHttpClient.newBuilder().readTimeout(SECOND, TimeUnit.SECONDS);
        mOkHttpClient.newBuilder().writeTimeout(SECOND, TimeUnit.SECONDS);
        mDelivery = new Handler(Looper.getMainLooper());
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static OkhttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkhttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkhttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 异步get
     */
    public void AsynGet(String url, final RequestCallback requestCallback) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        DebugLog.d(url);

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                DebugLog.e("---数据返回错误---" + e);
                processFailData(e, requestCallback);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                int code = response.code();
                if (code == 200) {
                    String responseBody = response.body().string();
                    String json = FormattingJson(responseBody);
                    DebugLog.d("get方法 responseBody---" + json);
                    JsonResult jsonResult = JSON.parseObject(responseBody, JsonResult.class);
                    processSuccessData(jsonResult, requestCallback);
                } else if (code == 500) {
                    JsonResult jsonResult = new JsonResult();
                    jsonResult.setCode("-1");
                    jsonResult.setMsg("后台服务器发生错误，请稍后重试");
                    processSuccessData(jsonResult, requestCallback);
                } else {
                    httpFailed(code, response);
                }

            }
        });
    }

    /**
     * 异步post 带body参数
     *
     * @param url
     * @param formBody
     * @param requestCallback
     */
    public void AsynPost(String url, FormBody formBody, final RequestCallback requestCallback) {

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        DebugLog.d(url);

        for (int i = 0; i < formBody.size(); i++) {
            DebugLog.d(formBody.name(i) + "---" + formBody.value(i));
        }

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DebugLog.e("---数据返回错误---" + e);
                processFailData(e, requestCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                int code = response.code();
                if (code == 200) {
                    String responseBody = response.body().string();
                    String json = FormattingJson(responseBody);
                    DebugLog.d("post方法 responseBody---" + json);
                    JsonResult jsonResult = JSON.parseObject(responseBody, JsonResult.class);
                    processSuccessData(jsonResult, requestCallback);
                } else if (code == 500) {
                    JsonResult jsonResult = new JsonResult();
                    jsonResult.setCode("-1");
                    jsonResult.setMsg("后台服务器发生错误，请稍后重试");
                    processSuccessData(jsonResult, requestCallback);
                } else {
                    httpFailed(code, response);
                }

            }
        });
    }

    /**
     * 异步post 不带参数
     *
     * @param url
     * @param requestCallback
     */
    public void AsynPostNoParams(String url, final RequestCallback requestCallback) {

        FormBody formBody = new FormBody.Builder()
                .build();
        DebugLog.d(url);

        //request需求
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        //异步执行
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DebugLog.e("---数据返回错误---" + e);
                processFailData(e, requestCallback);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                int code = response.code();
                if (code == 200) {
                    String responseBody = response.body().string();
                    String json = FormattingJson(responseBody);
                    DebugLog.d("post 方法--" + json);
                    JsonResult jsonResult = JSON.parseObject(responseBody, JsonResult.class);
                    processSuccessData(jsonResult, requestCallback);
                } else if (code == 500) {
                    JsonResult jsonResult = new JsonResult();
                    jsonResult.setCode("-1");
                    jsonResult.setMsg("后台服务器发生错误，请稍后重试");
                    processSuccessData(jsonResult, requestCallback);
                } else {
                    httpFailed(code, response);
                }

            }
        });
    }

    /**
     * 上传图片
     */
    public void updateImage(String url, String filepath, final RequestCallback requestCallback) {

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
        File file = new File(filepath);
        DebugLog.d(url);

        //图片内容
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", PrefersUtils.getString(ConstantString.TOKEN))
                .addFormDataPart("pic_info", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DebugLog.e("---数据返回错误e---" + e);
                processFailData(e, requestCallback);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                DebugLog.i("---图片上传 response---" + response);

                int code = response.code();
                if (code == 200) {
                    String responseBody = response.body().string();
                    String json = FormattingJson(responseBody);
                    DebugLog.d("图片上传 responseBody---" + json);
                    JsonResult jsonResult = JSON.parseObject(responseBody, JsonResult.class);
                    processSuccessData(jsonResult, requestCallback);
                } else if (code == 500) {
                    JsonResult jsonResult = new JsonResult();
                    jsonResult.setCode("-1");
                    jsonResult.setMsg("后台服务器发生错误，请稍后重试");
                    processSuccessData(jsonResult, requestCallback);
                }

            }
        });
    }

    /**
     * 上传多张图片
     */
    public void updateImages(String url, List<String> imgPaths, final RequestCallback requestCallback) {

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        DebugLog.d(url);

        // imgPaths 为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < imgPaths.size(); i++) {
            File file = new File(imgPaths.get(i));
            if (file != null) {
                builder.addFormDataPart("img", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                builder.addFormDataPart("key", ConstantString.key);
            }
        }
        MultipartBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DebugLog.e("---数据返回错误e---" + e);
                processFailData(e, requestCallback);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                DebugLog.i("---图片上传 response---" + response);

                int code = response.code();
                if (code == 200) {
                    String responseBody = response.body().string();
                    String json = FormattingJson(responseBody);
                    DebugLog.d("多图上传 responseBody---" + json);
                    JsonResult jsonResult = JSON.parseObject(responseBody, JsonResult.class);
                    processSuccessData(jsonResult, requestCallback);
                } else if (code == 500) {
                    JsonResult jsonResult = new JsonResult();
                    jsonResult.setCode("-1");
                    jsonResult.setMsg("后台服务器发生错误，请稍后重试");
                    processSuccessData(jsonResult, requestCallback);
                }
            }
        });


    }

    /**
     * 上传文件
     */
    public void uploadFile(String url, String filepath, final RequestCallback requestCallback) {

        DebugLog.d(url);
        MediaType MEDIA_TYPE_PNG = MediaType.parse("aac");
        File file = new File(filepath);
        //图片内容
        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("img", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
                .addFormDataPart("key", ConstantString.key)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DebugLog.e("---数据返回错误e---" + e);
                processFailData(e, requestCallback);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                DebugLog.i("---文件上传 response---" + response);

                int code = response.code();
                if (code == 200) {
                    String responseBody = response.body().string();
                    String json = FormattingJson(responseBody);
                    DebugLog.d("文件上传 responseBody---" + json);
                    JsonResult jsonResult = JSON.parseObject(responseBody, JsonResult.class);
                    processSuccessData(jsonResult, requestCallback);
                } else if (code == 500) {
                    JsonResult jsonResult = new JsonResult();
                    jsonResult.setCode("-1");
                    jsonResult.setMsg("后台服务器发生错误，请稍后重试");
                    processSuccessData(jsonResult, requestCallback);
                }


            }
        });


    }

    //处理数据返回失败
    private void processFailData(final IOException e, final RequestCallback requestCallback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                requestCallback.onFail(e);
            }
        });
    }

    //处理数据返回成功
    private void processSuccessData(final JsonResult jsonResult, final RequestCallback requestCallback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                try {

                    String code = jsonResult.getCode();
                    String msg = jsonResult.getMsg();
                    //判断不同的返回数据
                    if (Integer.valueOf(code) == 0 || Integer.valueOf(code) == 1) {
                        requestCallback.onSuccess(jsonResult.getData());
                    } else if (code.equals("20003")) {  // token 无效  或 过期

                        Context context = MyApplication.getContext();
//                        Intent loginIntent = new Intent(context, LoginActivity.class);
//                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(loginIntent);

                    } else {
                        ToastUtils.showMessage(msg);
                        requestCallback.onError();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //异步获取数据传递的接口
    public interface RequestCallback {

        void onFail(IOException e);

        void onSuccess(String data) throws IOException, InterruptedException;

        void onError() throws IOException;

        void onNoData();

    }

    public void httpFailed(final int errorNum, final Response response) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                DebugLog.e(errorNum + "");
                if (errorNum == 409 || errorNum == 401) {
                    PrefersUtils.clear();
                    PrefersUtils.putBoolean(ConstantString.IS_FIRST, true);

                    if (errorNum == 409) {
                        ToastUtils.showMessage("您的账号在另一台设备上登录，请重新登录");
                    } else if (errorNum == 401) {
                        ToastUtils.showMessage("你还未登录，请登录");
                    }

                    Context context = MyApplication.getInstance().getApplicationContext();
                    Intent loginIntent = new Intent(context, MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    loginIntent.putExtra("from", 1);
                    context.startActivity(loginIntent);
                } else if (errorNum == 425) {
                    String author = response.header("WWW-Authenticate", "");
                    PrefersUtils.putString(ConstantString.TOKEN, author);
                } else {
                    ToastUtils.showMessage("网络繁忙，请稍后重试");
                }
            }
        });
    }

    /**
     * 创建时间：2017/11/6 11:32  描述：格式化 json 数据
     */
    private static String FormattingJson(String json) {
        if (json == null || json.length() == 0) {
            return "Empty/Null json content";
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                return message;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                return message;
            }
            return "Invalid Json" + "\n" + json;
        } catch (JSONException e) {
            return "Invalid Json" + "\n" + json;
        }
    }
}