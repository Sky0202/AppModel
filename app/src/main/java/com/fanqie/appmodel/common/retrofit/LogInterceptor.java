package com.fanqie.appmodel.common.retrofit;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.fanqie.appmodel.common.utils.DebugLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by zpw on 2017/12/25.
 * 网络请求 log 拦截器
 */

public class LogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        DebugLog.d("before chain,request()");
        Request request = chain.request();
        Response response;
        try {
            long t1 = System.nanoTime();
            response = chain.proceed(request);
            long t2 = System.nanoTime();
            double time = (t2 - t1) / 1e6d;
            String acid = request.url().queryParameter("ACID");     //本项目log特定参数项目接口acid
            String userId = request.url().queryParameter("userId"); //本项目log特定参数用户id
            String type = "";
            if (request.method().equals("GET")) {
                type = "GET";
            } else if (request.method().equals("POST")) {
                type = "POST";
            } else if (request.method().equals("PUT")) {
                type = "PUT";
            } else if (request.method().equals("DELETE")) {
                type = "DELETE";
            }
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("\n-------------------- " + (TextUtils.isEmpty(acid) ? "" : acid) + "  begin--------------------")
                    .append("\nmethod--> ").append(type)
                    .append("\nacid--> ").append(TextUtils.isEmpty(acid) ? "" : acid)
                    .append("\nuserId--> ").append(TextUtils.isEmpty(userId) ? "" : userId)
                    .append("\nnetwork code--> ").append(response.code() + "")
                    .append("\nURL--> ").append(request.url() + "")
                    .append("\ntime--> ").append(time + "")
                    .append("\nrequest headers--> ").append(request.headers() + "")
                    .append("\nrequest--> ").append(bodyToString(request.body()))
                    .append("\nresponse--> ").append(FormattingJson(buffer.clone().readString(UTF8)));

            DebugLog.d(stringBuilder.toString());


        } catch (Exception e) {
            throw e;
        }
        return response;
    }

    /**
     * 创建时间：2017/12/27 10:05
     * <p>
     * 描述：请求数据转 string 并格式化打印
     *
     * @author zpw
     */
    private static String bodyToString(final RequestBody request) {

        try {
            final Buffer buffer = new Buffer();

            if (request != null) {

                request.writeTo(buffer);
                // 获得 string
                String str = buffer.readUtf8();
                str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                URLDecoder urlDecoder = new URLDecoder();
                // URL 解码
                String decodeStr = urlDecoder.decode(str, "UTF-8");
                String[] splitStr = decodeStr.split("&");

                List<String> list = new ArrayList<>();
                for (int i = 0; i < splitStr.length; i++) {
                    list.add(splitStr[i]);
                }
                String result = FormattingJson(JSON.toJSONString(list));
                return result;
            } else {
                return "request not work";
            }

        } catch (final IOException e) {
            return "did not work";
        }
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
                String message = jsonObject.toString(2);
                return message;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(2);
                return message;
            }
            return "Invalid Json" + "\n" + json;
        } catch (JSONException e) {
            return "Invalid Json" + "\n" + json;
        }
    }
}



