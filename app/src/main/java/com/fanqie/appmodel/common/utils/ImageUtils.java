package com.fanqie.appmodel.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fanqie.appmodel.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * 创建时间：2017/5/9 16:04  描述：相册相机 选择 工具类
 */

public class ImageUtils extends TakePhotoActivity {

    /**
     * glide加载圆形图片
     */
    public static void loadCirclePic(final Context context, String url, final ImageView iv_show) {

        //加载圆角图片
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .error(R.drawable.tx)
                .into(new BitmapImageViewTarget(iv_show) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_show.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 弹出选择相机popuwindow
     */
    public static void showChooseCarme(final AppCompatActivity activity, View viewz) {

        ImageGetUtils imageGetUtils = new ImageGetUtils(activity);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.popupwindow_carme, null);

        //所有按钮的监听事件
        TextView tv_carme_popuwindow = (TextView) view.findViewById(R.id.tv_carme_popuwindow);
        TextView tv_image_popuwindow = (TextView) view.findViewById(R.id.tv_image_popuwindow);
        TextView tv_cancel_popuwindow = (TextView) view.findViewById(R.id.tv_cancel_popuwindow);


        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //点击事件
        clickListener(imageGetUtils, tv_carme_popuwindow, tv_image_popuwindow, popupWindow);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.3f;
        if (lp.alpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        activity.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //消去遮罩
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);

            }
        });
        //监听取消按钮
        tv_cancel_popuwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popupWindow.showAtLocation(viewz, Gravity.BOTTOM, 0, 50);
    }

    /**
     * popuwindows 点击事件
     */
    public static void clickListener(final ImageGetUtils imageGetUtils, TextView tv_carme, TextView tv_image, final PopupWindow popupWindow) {

        tv_carme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageGetUtils.getImageFromCamera();
                popupWindow.dismiss();
            }
        });

        tv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageGetUtils.getImageFromGallery();
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 创建时间：2017/4/14 16:24  描述：takePhoto 进行多选单选,以及拍照
     *
     * @param picNum 多选最大张数
     */
    public static void showChoosePicture(final AppCompatActivity activity, final TakePhoto takePhoto, final int picNum) {

        File file = new File(Environment.getExternalStorageDirectory(), "/jingbei/image/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        final Uri imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        // 设置旋转角度  是否纠正
        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setCorrectImage(true).create());

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflateView = layoutInflater.inflate(R.layout.popupwindow_carme, null);

        //所有按钮的监听事件
        TextView tv_carme_popuwindow = (TextView) inflateView.findViewById(R.id.tv_carme_popuwindow);
        TextView tv_image_popuwindow = (TextView) inflateView.findViewById(R.id.tv_image_popuwindow);
        TextView tv_cancel_popuwindow = (TextView) inflateView.findViewById(R.id.tv_cancel_popuwindow);


        final PopupWindow popupWindow = new PopupWindow(inflateView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.3f;
        if (lp.alpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        activity.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //消去遮罩
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);

            }
        });

        // 相机
        tv_carme_popuwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto.onPickFromCapture(imageUri);
                popupWindow.dismiss();
            }
        });

        // 相册
        tv_image_popuwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picNum == 1) {
                    takePhoto.onPickFromGallery();
                    popupWindow.dismiss();
                } else {
                    takePhoto.onPickMultiple(picNum);
                    popupWindow.dismiss();
                }
            }
        });

        //监听取消按钮
        tv_cancel_popuwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popupWindow.showAtLocation(inflateView, Gravity.BOTTOM, 0, 50);

    }

    private static void configCompress(TakePhoto takePhoto) {

        int maxSize = 500 * 1024;
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, false);
    }

}
