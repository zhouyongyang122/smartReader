package com.qudiandu.smartreader.thirdParty.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qudiandu.smartreader.ui.main.activity.SRMainActivity;
import com.qudiandu.smartreader.ui.set.activity.SRSysMsgActivity;
import com.qudiandu.smartreader.ui.web.SRWebViewActivity;
import com.qudiandu.smartreader.utils.ZYLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ZY on 17/10/14.
 */

public class SRJPushSelfReceiver extends BroadcastReceiver {

    private static final String TAG = "SRJPushSelfReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            ZYLog.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                ZYLog.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                ZYLog.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                ZYLog.e(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                ZYLog.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                ZYLog.e(TAG, "[MyReceiver] 用户点击打开了通知");

                Intent localIntent = null;
                try {
                    String msgJson = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                    Gson gson = new Gson();
                    SRJpushMsg jpushMsg = gson.fromJson(msgJson, SRJpushMsg.class);
                    if (jpushMsg.type.equals(SRJpushMsg.OPEN_TYPE)) {
                        localIntent = new Intent(context, SRMainActivity.class);
                    } else if (jpushMsg.type.equals(SRJpushMsg.SYSTME_TYPE)) {
                        localIntent = new Intent(context, SRSysMsgActivity.class);
                    } else if (jpushMsg.type.equals(SRJpushMsg.URL_TYPE)) {
                        localIntent = SRWebViewActivity.createIntent(context, jpushMsg.data.url, "");
                    }
                    localIntent = new Intent(context, SRMainActivity.class);
                } catch (Exception e) {
                    localIntent = new Intent(context, SRMainActivity.class);
                }

                //打开自定义的Activity
                localIntent.putExtras(bundle);
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(localIntent);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                ZYLog.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                ZYLog.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                ZYLog.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    ZYLog.e(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
