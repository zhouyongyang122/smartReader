package com.qudiandu.smartreader.base.html5;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.qudiandu.smartreader.ui.main.activity.SRMainActivity;
import com.qudiandu.smartreader.ui.web.SRWebViewActivity;
import com.qudiandu.smartreader.utils.ZYLog;

import java.util.Set;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 上午10:27$
 */
public class ZYSchemeParser {

    //"SmartReader://invite";

    private static final String TAG = "ZYSchemeParser";

    public static final String APP_NAME = "SmartReader";

    //邀请好友
    private static final String PLAY_COURSE = "invite";

    private static final String WEBVIEW = "webview";

    /**
     * pageIntent,需要跳转到的界面
     */
    private String pageIntent;

    /**
     * 获取跳转页面的Intent
     *
     * @param context
     * @param strUri
     * @return
     */
    public Intent getActionIntent(Context context, String strUri) {
        return getActionIntent(context, strUri, false);
    }

    public Intent getActionIntent(Context context, String strUri, boolean processHttp) {
        Intent intent = null;
        try {

            if (context == null) {
                return null;
            }

            if (processHttp && strUri != null && strUri.startsWith("http")) {
                return SRWebViewActivity.createIntent(context, strUri, "");
            }

            boolean pareseResult = parseUri(strUri);
            if (!pareseResult) {
                return null;
            }

            if (pageIntent.equalsIgnoreCase(PLAY_COURSE)) {
//                intent = FZOCourseActivity.createIntent(context, couserId);
            } else if (pageIntent.equals(WEBVIEW)) {
                try {
                    intent = SRWebViewActivity.createIntent(context, strUri.substring(strUri.indexOf("url=") + 4), "");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            ZYLog.d(TAG, "getActionIntent-error: " + e.getMessage());
            if (context != null) {
                context.startActivity(SRMainActivity.createIntent(context));
                intent = null;
            }
        }

        return intent;
    }

    boolean parseUri(String strUri) {
        ZYLog.e(TAG, "scheme-url: " + strUri);
        if (strUri == null) {
            ZYLog.d(TAG, "parseUri strUri == null");
            return false;
        }
        Uri uri = Uri.parse(strUri);
        if (uri == null) {
            ZYLog.d(TAG, "parseUri context == null");
            return false;
        }
        String scheme = uri.getScheme();
        if (scheme == null || !scheme.equals(APP_NAME)) {
            ZYLog.d(TAG, "parseUri scheme == null");
            return false;
        }
        String page = uri.getHost();
        if (page == null) {
            ZYLog.d(TAG, "parseUri page == null");
            return false;
        }
        pageIntent = page.trim();
        Set<String> paramNames = uri.getQueryParameterNames();
        if (paramNames != null) {
            for (String name : paramNames) {
                if (name == null) {
                    continue;
                }
//                if (name.equalsIgnoreCase(COURSEID)) {
////                    couserId = StringUtil.putLong(uri.getQueryParameter(name));
//                }
            }
        }
        return true;
    }
}
