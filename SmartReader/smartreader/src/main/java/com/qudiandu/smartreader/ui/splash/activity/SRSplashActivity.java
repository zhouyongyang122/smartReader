package com.qudiandu.smartreader.ui.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.ZYPreferenceHelper;
import com.qudiandu.smartreader.service.downNet.down.ZYDownState;
import com.qudiandu.smartreader.ui.main.activity.SRMainActivity;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.utils.ZYLog;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/15.
 */

public class SRSplashActivity extends ZYBaseActivity {

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
        }, 1000);

        if (!ZYPreferenceHelper.getInstance().isInsertDefBook()) {
            SRBook book = new SRBook();
            book.setBook_id("0");
            book.setName("测试书籍");
            book.setGrade_id("11");
            book.setPic("file:///android_asset/1/title.jpg");
            book.setGrade("一年级");
            book.setUrl("file:///android_asset/1/");
            book.setUpdate_time("1490527877");
            book.setState(ZYDownState.FINISH);
            book.setSavePath("file:///android_asset/1/");
            ZYLog.e(getClass().getSimpleName(), "insertDefBook: " + book.save());
            ZYPreferenceHelper.getInstance().setInsertDefBook(true);
            ZYPreferenceHelper.getInstance().setShowTractBg(true);
            ZYPreferenceHelper.getInstance().setShowTractTrans(true);
        }
    }

    @Override
    protected boolean tintStatusBar() {
        return false;
    }
}
