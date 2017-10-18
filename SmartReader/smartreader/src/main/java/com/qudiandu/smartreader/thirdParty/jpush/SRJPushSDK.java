package com.qudiandu.smartreader.thirdParty.jpush;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.client.android.BuildConfig;

/**
 * Created by ZY on 17/10/14.
 */

public class SRJPushSDK {

    public static void init(Context context) {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(context);            // 初始化 JPush
    }
}
