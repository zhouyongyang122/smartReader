package com.smartreader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.qiniu.auth.Authorizer;
import com.qiniu.io.IO;
import com.qiniu.rs.CallBack;
import com.qiniu.rs.PutExtra;

import java.util.HashMap;

/**
 * Created by ZY on 17/3/14.
 */

public class ZYUtils {

    /**
     * 上传文件到七牛
     *
     * @param context  context
     * @param filePath 文件路径
     * @param token    上传token
     * @param callBack 回调
     * @return 0 成功 1 失败
     */
    public static int uploadFile(Context context, String filePath, String token, CallBack callBack) {
        // 生成key
        String key = ZYFileUtils.getFileName(filePath);

        return uploadFile(context, key, filePath, token, callBack);
    }

    /**
     * 上传文件到七牛
     *
     * @param context  context
     * @param filePath 文件路径
     * @param token    上传token
     * @param callBack 回调
     * @return 0 成功 1 失败
     */
    public static int uploadFile(Context context, String key, String filePath, String token, CallBack callBack) {
        if (context == null || TextUtils.isEmpty(filePath) || TextUtils.isEmpty(token) || callBack == null) {
            return -1;
        }
        // 生成key
        if (TextUtils.isEmpty(key)) {
            return -1;
        }

        Authorizer auth = new Authorizer();
        auth.setUploadToken(token);
        PutExtra extra = new PutExtra();
        extra.params = new HashMap<>();
        extra.params.put("x:a", "测试中文信息");
        Uri url = new Uri.Builder().path(filePath).build();

        IO.putFile(context, auth, key, url, extra, callBack);

        return 0;
    }

    /**
     * 描述：判断网络是否有效.
     *
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
}
