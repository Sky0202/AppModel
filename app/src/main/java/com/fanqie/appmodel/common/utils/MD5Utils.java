package com.fanqie.appmodel.common.utils;

import android.annotation.SuppressLint;

import java.security.MessageDigest;

public class MD5Utils {

    @SuppressLint("DefaultLocale")
    public static String encrypt(String plainText) {
        String str = "";
        try {
            // 实例化一个算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 加密文本
            md.update(plainText.getBytes());
            byte[] b = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
        }
        return str.toUpperCase();
    }

    public static void main(String[] args) {
    }
}
