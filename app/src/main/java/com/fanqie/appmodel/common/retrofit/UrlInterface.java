package com.fanqie.appmodel.common.retrofit;

import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/12/25.
 * URL 接口类
 */

public interface UrlInterface {

    // 上传图片
    @POST("client/common/uploadImg")
    @Multipart
    Observable<HttpResult<JSONObject>> upload(
            @Part MultipartBody.Part file
    );

    // 下载
    @GET
    @Streaming
    Observable<Response<ResponseBody>> download(@Url String url);




}