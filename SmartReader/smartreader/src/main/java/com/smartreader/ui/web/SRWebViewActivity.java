package com.smartreader.ui.web;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smartreader.BuildConfig;
import com.smartreader.R;
import com.smartreader.base.mvp.ZYBaseActivity;
import com.smartreader.utils.ZYSystemUtils;
import com.smartreader.utils.ZYToast;

import java.util.UUID;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/5.
 */

public class SRWebViewActivity extends ZYBaseActivity {

    static final String URL = "url";

    static final String TITLE = "title";

    public static Intent createIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, SRWebViewActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        return intent;

    }

    @Bind(R.id.webView)
    WebView webview;

    SRWebChromeClient chromeClient;

    SRWebViewClient viewClient;

    private String url;

    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_web);

        url = getIntent().getStringExtra(URL);

        if (TextUtils.isEmpty(url)) {
            ZYToast.show(this, "url不能为空!");
            finish();
            return;
        }

        title = getIntent().getStringExtra(TITLE);

        if (!TextUtils.isEmpty(title)) {
            mActionBar.showTitle(title);
        }

        mActionBar.mIvLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initWeb();

        webview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
            return;
        }
        super.onBackPressed();
    }

    private void initWeb() {
        chromeClient = new SRWebChromeClient();
        viewClient = new SRWebViewClient();

        WebSettings webSettings = webview.getSettings();
        if (null == webSettings) {
            finish();
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString(webSettings.getUserAgentString() + ";APP-VERSION=" + ZYSystemUtils.getAppVersionName(this));

        webview.setWebChromeClient(chromeClient);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            //浏览器高度模式
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webview.setWebViewClient(viewClient);
    }

    class SRWebChromeClient extends WebChromeClient {

    }

    class SRWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!TextUtils.isEmpty(view.getTitle())) {
                mActionBar.showTitle(view.getTitle());
            }
        }
    }
}
