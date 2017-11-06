package com.fanqie.appmodel.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片获取工具类
 * <p>
 * 功能：
 * 从图库中获取图片 getImageFromGallery
 * 从相机获取图片 getImageFromCamera
 * <p>
 * 添加权限：
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" /> //sd卡创建文件权限
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> //sd卡读写权限
 * <uses-permission android:name="android.permission.CAMERA" /> // 访问相机设备
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> //内外置sd卡写权限-
 * <p>
 * 图片处理：
 * 图片处理使用glide图片库 加入依赖
 * <p>
 * 其他问题：
 * 加入activity时不要直接填写this
 * <p>
 * Created by Administrator on 2016/6/25.
 */
public class ImageGetUtils {

    private AppCompatActivity appCompatActivity;
    private Fragment fragment;
    private String savePath = "sdcard/jingbei/image/";//存放路径
    private String tempFileName;//临时文件路径
    private String tempPhotoPath = "";//选择照片或者或拍照完成后保存的照片地址
    private String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    public ImageGetUtils(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public ImageGetUtils(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * 从相册获取
     */
    public void getImageFromGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE_UNSPECIFIED);
        if (appCompatActivity == null) {
            fragment.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        } else {
            appCompatActivity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        }
    }


    /*
     * 从拍照获取
     */
    public void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        tempFileName = System.currentTimeMillis() + ".png";//用当前时间命名
        if (hasSdcard()) {
            //设定拍照存放到自己指定的目录,可以先建好
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(savePath, tempFileName)));
        }

        if (appCompatActivity == null) {
            fragment.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
        } else {
            appCompatActivity.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
        }

    }

    /**
     * 处理返回结果 返回图片路径
     */
    public String ProcessResults(int requestCode, int resultCode, Intent data) {
//        Uri uri = null;
        if (requestCode == PHOTO_REQUEST_GALLERY) {//返回图库
            if (data != null) {

                Uri uri = data.getData();
                if (appCompatActivity == null) {
                    tempPhotoPath = getRealPathFromURI(uri, fragment.getActivity());
                } else {
                    tempPhotoPath = getRealPathFromURI(uri, appCompatActivity);
                }

            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {//返回相机
            //1.好些设备在调用系统相机照相后返回的 data为null
            //解决办法就是在调用相机之前先设定好照片的路径和名称,拍完后直接拿来用
            //2.如果图片地址没有图片存在,则表示没有进行照相
            tempPhotoPath = savePath + tempFileName;
            File file = new File(tempPhotoPath);
            tempPhotoPath = file.getAbsolutePath();
            //图片压缩处理
            File compress = BitMapCompressUtils.compress(tempPhotoPath, 500, 500, 200 * 1024);
            String tempPhotoPathCompress = compress.getPath();
            //图片翻转处理
            int bitmapDegree = getBitmapDegree(tempPhotoPath);
            Bitmap bitmap = BitmapFactory.decodeFile(tempPhotoPathCompress);
            Bitmap bitmap1 = rotateBitmapByDegree(bitmap, bitmapDegree);
            tempPhotoPath = saveBitmapFile(bitmap1, tempPhotoPathCompress);
        }
        return tempPhotoPath;
    }

    public String saveBitmapFile(Bitmap bitmap, String path) {
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            //从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            //获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            if (bm != null) {
                returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            }
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    /**
     * 是否有sd卡
     */
    private static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

//    /**
//     * 获取uri的文件地址
//     */
//    private String getRealPathFromURI(Uri contentUri, Context context) {
//        DebugLog.i("zzz", "--从图库获取图片uri--" + contentUri);
//        DebugLog.i("zzz", "--从图库获取图片context--" + context);
//        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

    /**
     * 通过uri获取图片
     *
     * @param ac
     * @param uri
     * @return
     */
    public String getRealPathFromURI(Uri uri, Context ac) {
        if (uri.getScheme().toString().compareTo("content") == 0) {
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath).getAbsolutePath();
                }
            }
        } else if (uri.getScheme().toString().compareTo("file") == 0) {
            return new File(uri.toString().replace("file://", "")).getAbsolutePath();
        }
        return null;
    }

}
