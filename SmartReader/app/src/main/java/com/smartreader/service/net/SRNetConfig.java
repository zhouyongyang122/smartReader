package com.smartreader.service.net;

import android.os.Build;

import com.smartreader.BuildConfig;
import com.smartreader.SRApplication;
import com.smartreader.common.utils.SRChannelUtil;
import com.smartreader.common.utils.SRDeviceIDUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZY on 17/3/16.
 */

public class SRNetConfig {

    public static final String API_URL = BuildConfig.API_URL;

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("App-Version", BuildConfig.VERSION_NAME);
        headers.put("Client-OS", headerFormat(Build.VERSION.RELEASE));
        headers.put("Device-Model", headerFormat(Build.MODEL));
        headers.put("Umeng-Channel", SRChannelUtil.getChannel(SRApplication.getInstance()));
        headers.put("User-Agent", "android");
        headers.put("versionCode", BuildConfig.VERSION_CODE + "");
        headers.put("idfa", SRDeviceIDUtil.getInstance().getDeviceID(SRApplication.getInstance()));
        return headers;
    }

    public HashMap<String, String> getDefParams() {
        HashMap<String, String> params = new HashMap<>();
//        params.put("uid", "");
//        params.put("token", "");
        return params;
    }

    public static String headerFormat(String value) {
        if (value == null) {
            return "null";
        }
        String newValue = value.replace("\n", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
