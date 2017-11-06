package com.fanqie.appmodel.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitMapCompressUtils {

    public static final String TAG = "ehome" + BitMapCompressUtils.class.getName();

    public static File compress(String path , int tagerX, int tagerY, long tagerSize) {

//        String path = getRealPathFromURI(fileUri);
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = tagerSize;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int scale = sumScaleSize(options.outWidth, options.outHeight, tagerX, tagerY); // 图片分辨率压缩比例
            DebugLog.d("图片分辨率压缩系数:" + scale);
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            int compressSzie = 100; // 图片质量压缩比例
//            float tem = (bitmap.getByteCount() / tagerSize);
//            if (tem < 1) {
//                compressSzie = 1;
//            } else {
//                compressSzie = (int) (tem + 0.5f);
//            }
            outputFile = createImageFile();
            FileOutputStream fos = null;
            ByteArrayOutputStream bOut = null;
            try {
                bOut = new ByteArrayOutputStream();
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
                DebugLog.d("图片质量压缩系数：" + compressSzie);
                DebugLog.d("图片大小：" + bOut.size());
                while (bOut.size() > tagerSize) {  //如果压缩后大于100Kb，则提高压缩率，重新压缩
                    compressSzie -= 10;
                    DebugLog.d("图片质量压缩系数：" + compressSzie);
                    bOut.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressSzie, bOut);
                    DebugLog.d("图片大小：" + bOut.size());
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressSzie, fos);
                bOut.close();
                fos.close();
                bOut = null;
                fos = null;
            } catch (IOException e) {
                DebugLog.d("图片压缩后失败");
                e.printStackTrace();
                bOut = null;
                fos = null;
            }
//            DebugLog.d("图片压缩后大小" + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        return outputFile;
    }

    /**
     * 计算压缩比率
     *
     * @return
     */
    private static int sumScaleSize(int originalX, int originalY, int tagerX, int tagerY) {

        int res = 1;
        if (originalX > tagerX || originalY > tagerY) {
            float scaleX = originalX / tagerX;
            float scaleY = originalY / tagerY;
            float scale = scaleX > scaleY ? scaleY : scaleX;
            res = Math.round(scale + 0.5f); // 最小的数，这样保证一遍能够达到 目标尺寸
        }
        return res;
    }

    /**
     * 生成新的图片地址
     *
     * @return
     */
    private static File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/com.fanqie.aoqiyiyao/image");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
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
