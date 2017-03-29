package com.smartreader.ui.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.smartreader.R;
import com.smartreader.ZYPreferenceHelper;
import com.smartreader.service.downNet.down.ZYDownState;
import com.smartreader.ui.main.activity.SRMainActivity;
import com.smartreader.base.mvp.ZYBaseActivity;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.utils.ZYLog;

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
        }, 2000);

        if (!ZYPreferenceHelper.getInstance().isInsertDefBook()) {
            SRBook book = new SRBook();
            book.setBook_id(0);
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
        }
    }

    @Override
    protected boolean tintStatusBar() {
        return false;
    }
}
