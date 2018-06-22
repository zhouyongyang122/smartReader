package com.qudiandu.smartdub.ui.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.ZYPreferenceHelper;
import com.qudiandu.smartdub.service.downNet.down.ZYDownState;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.main.activity.SRMainActivity;
import com.qudiandu.smartdub.base.mvp.ZYBaseActivity;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;
import com.qudiandu.smartdub.utils.ZYLog;
import com.qudiandu.smartdub.utils.ZYSystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;

import static com.qudiandu.smartdub.SRApplication.BOOK_ROOT_DIR;
import static com.qudiandu.smartdub.ui.main.model.SRIJKPlayManager.DEF_BOOK_MP3_PATH;

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

        if (!ZYPreferenceHelper.getInstance().isLogoutVersonCode16() && ZYSystemUtils.getAppVersionCode(this) <= 16) {
            ZYPreferenceHelper.getInstance().saveLogoutVersonCode16();
            if (!SRUserManager.getInstance().isGuesterUser(false)) {
                SRUserManager.getInstance().loginOut();
            }
        }

        copyDefBookToSdcard();
    }

    @Override
    protected boolean tintStatusBar() {
        return false;
    }

    //ijkplayer播放assets文件有问题 把默认书籍文件放到sdcard
    void copyDefBookToSdcard() {
        File file = new File(DEF_BOOK_MP3_PATH);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            return;
        }

        try {
            String[] fileNames = getAssets().list("1/mp3");
            for (String name : fileNames) {
                file = new File(DEF_BOOK_MP3_PATH + "/" + name);
                if (!file.exists()) {
                    file.createNewFile();
                }
                InputStream inputStream = getAssets().open("1/mp3/" + name);
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                inputStream.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
