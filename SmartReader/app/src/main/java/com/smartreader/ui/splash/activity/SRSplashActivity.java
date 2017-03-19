package com.smartreader.ui.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.smartreader.R;
import com.smartreader.ui.main.activity.SRMainActivity;
import com.smartreader.base.mvp.SRBaseActivity;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/15.
 */

public class SRSplashActivity extends SRBaseActivity {

    @Bind(R.id.splashImg)
    ImageView mSplashImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_splash);
        hideActionBar();
        mSplashImg.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SRSplashActivity.this, SRMainActivity.class));
                finish();
            }
        }, 2000);
    }

    @Override
    protected boolean tintStatusBar() {
        return false;
    }
}
