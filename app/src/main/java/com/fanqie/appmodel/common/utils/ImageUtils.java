package com.fanqie.appmodel.common.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fanqie.appmodel.R;
import com.fanqie.appmodel.common.application.MyApplication;
import com.fanqie.appmodel.common.constants.ConstantString;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * 创建时间：2017/5/9 16:04  描述：相册相机 选择 工具类
 */

public class ImageUtils extends TakePhotoActivity {

    /**
     * glide加载圆形图片
     */
    public static void loadCirclePic(final Context context, String url, final ImageView iv_show) {

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.tx)
                .transforms(new CircleCrop());

        //加载圆角图片
        Glide.with(MyApplication.getContext())
                .load(url)
                .apply(requestOptions)
                .into(iv_show);

    }

    /**
     * 创建时间：2017/4/14 16:24  描述：takePhoto 进行多选单选,以及拍照
     *
     * @param picNum 多选最大张数
     */
    public static void showChoosePicture(Context context, TakePhoto takePhoto, int picNum) {

        configCompress(takePhoto);
        // 设置旋转角度  是否纠正
        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setCorrectImage(true).create());

        showPopWindow(context, takePhoto, picNum, false);

    }

    /**
     * 创建时间：2017/12/21 10:07
     * <p>
     * 描述：选择照片后是否裁剪
     *
     * @author zpw
     */
    public static void showChoosePicture(Context context, TakePhoto takePhoto, int picNum, boolean cut) {

        configCompress(takePhoto);
        // 设置旋转角度  是否纠正
        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setCorrectImage(true).create());
        showPopWindow(context, takePhoto, picNum, cut);

    }

    private static void showPopWindow(Context context, final TakePhoto takePhoto, final int picNum, final boolean cut) {

        File file = new File(Environment.getExternalStorageDirectory(), ConstantString.FILE_PATH + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        final Uri imageUri = Uri.fromFile(file);

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.popupwindow_carme);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);

        window.setWindowAnimations(R.style.dialogAni);

        //所有按钮的监听事件
        TextView tv_carme_popuwindow = window.findViewById(R.id.tv_carme_popuwindow);
        TextView tv_image_popuwindow = window.findViewById(R.id.tv_image_popuwindow);
        TextView tv_cancel_popuwindow = window.findViewById(R.id.tv_cancel_popuwindow);

        // 相机
        tv_carme_popuwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cut) {
                    takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                } else {
                    takePhoto.onPickFromCapture(imageUri);
                }
                dialog.dismiss();
            }
        });

        // 相册
        tv_image_popuwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picNum == 1 && !cut) {
                    takePhoto.onPickFromGallery();
                } else if (picNum == 1 && cut) {
                    takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                } else if (!cut) {
                    takePhoto.onPickMultiple(picNum);
                } else {
                    takePhoto.onPickMultipleWithCrop(picNum, getCropOptions());
                }
                dialog.dismiss();
            }
        });

        //监听取消按钮
        tv_cancel_popuwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    // 裁剪参数配置
    private static CropOptions getCropOptions() {

        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(1080).setOutputY(800);
        builder.setWithOwnCrop(true);
        return builder.create();

    }

    // 压缩参数配置
    private static void configCompress(TakePhoto takePhoto) {

        int maxSize = 500 * 1024;
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, false);
    }

}
