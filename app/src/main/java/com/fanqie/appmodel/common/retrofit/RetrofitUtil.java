package com.fanqie.appmodel.common.retrofit;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.alibaba.fastjson.JSONObject;
import com.fanqie.appmodel.R;
import com.fanqie.appmodel.common.application.MyApplication;
import com.fanqie.appmodel.common.constants.ConstantString;
import com.fanqie.appmodel.common.utils.DebugLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/12/25.
 * retrofit 基础封装类
 */

public class RetrofitUtil {

    private Retrofit retrofit;
    private UrlInterface urlInterface;

    private static RetrofitUtil instance = null;

    private RetrofitUtil() {

        // 超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LogInterceptor());
        builder.connectTimeout(10, TimeUnit.SECONDS);

        // 构建 retrofit
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ConstantString.BASE_URL)
                .build();

        urlInterface = retrofit.create(UrlInterface.class);

    }

    // 单例模式  加同步 保安全
    private static synchronized RetrofitUtil getInstance() {
        if (instance == null) {
            instance = new RetrofitUtil();
        }
        return instance;
    }

    public static UrlInterface getService() {
        return getInstance().urlInterface;
    }

    /**
     * 创建时间：2017/12/25 17:31
     * <p>
     * 描述：上传图片
     *
     * @author zpw
     */
    public static void upLoad(String filePath, BaseObserver<JSONObject> observer) {

        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("img", file.getName(), requestBody);

        getService().upload(filePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 创建时间：2018/1/4 16:29
     * <p>
     * 描述：下载文件
     *
     * @author zpw
     */
    public static void download(String url) {

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyApplication.getContext(), "down");
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("正在下载最新安装包");
        final NotificationManager manager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        notificationBuilder.setOngoing(true);

        // 超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response orginalResponse = chain.proceed(chain.request());
                return orginalResponse.newBuilder()
                        .body(new ProgressResponseBody(orginalResponse.body(), new ProgressResponseBody.ProgressListener() {
                            @Override
                            public void onProgress(long progress, long total, boolean done) {
                                // 下载进度
                                double pro = (float) progress / (float) total;
                                DebugLog.d(pro + "%");

                                if (pro == 1.0) {

                                    notificationBuilder.setProgress(0, 0, false);
                                    notificationBuilder.setContentText("下载完毕");
                                    manager.notify(111, notificationBuilder.build());
                                    manager.cancel(111);

                                } else {

                                    notificationBuilder.setProgress(100, (int) (pro * 100), false);
                                    notificationBuilder.setContentText((int) (pro * 100) + "%");
                                    manager.notify(111, notificationBuilder.build());
                                }
                            }
                        })).build();
            }
        });

        // 构建 retrofit
        getService().download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBody) throws Exception {
                        // 下载成功
                        writeToDisk(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 下载失败
                        DebugLog.e(throwable.getMessage());
                    }
                });

    }

    /**
     * 创建时间：2018/1/4 16:31
     * <p>
     * 描述：将下载的文件写入手机
     *
     * @author zpw
     */
    private static void writeToDisk(Response<ResponseBody> response) {

        File outFile = new File(Environment.getExternalStorageDirectory() + File.separator + ConstantString.APK_PATH);
        if (!outFile.exists()) {
            // 创建文件夹
            outFile.mkdirs();
        }

        InputStream is = response.body().byteStream();
        File apkFile = new File(outFile.getAbsolutePath(), ConstantString.APK_NAME);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(apkFile);
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            DebugLog.d("apk存储成功");
            installApk(apkFile);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建时间：2018/1/5 10:42
     * <p>
     * 描述：安装 apk
     *
     * @param apkFile 安装包
     * @author zpw
     */
    private static void installApk(File apkFile) {

        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  android 7.0 以上
            Uri uriForFile = FileProvider.getUriForFile(MyApplication.getContext(),
                    MyApplication.getContext().getPackageName() + ".fileprovider", apkFile);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        } else {
            // 7.0 以下
            i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        MyApplication.getContext().startActivity(i);
    }

}
