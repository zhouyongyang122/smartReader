package com.qudiandu.smartdub.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartdub.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartdub.thirdParty.xiansheng.XianShengSDK;
import com.qudiandu.smartdub.ui.main.model.SRIJKPlayManager;
import com.qudiandu.smartdub.ui.main.presenter.SRBookHomePresenter;
import com.qudiandu.smartdub.ui.main.view.SRBookHomeFragment;
import com.qudiandu.smartdub.utils.ZYLog;

/**
 * Created by ZY on 17/3/29.
 */

public class SRBookHomeActivity extends ZYBaseFragmentActivity<SRBookHomeFragment> {
    static final String LOCAL_PATH = "localPath";

    public static Intent createIntent(Context context, String localPath) {
        Intent intent = new Intent(context, SRBookHomeActivity.class);
        intent.putExtra(LOCAL_PATH, localPath);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();

        String localPath = getIntent().getStringExtra(LOCAL_PATH);

        ZYLog.e(getClass().getSimpleName(), "localPath: " + localPath);

        new SRBookHomePresenter(mFragment, localPath);
    }

    @Override
    protected SRBookHomeFragment createFragment() {
        return new SRBookHomeFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onBackPressed() {
        if (!mFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected boolean tintStatusBar() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SRIJKPlayManager.getInstance().setPagePlayListener(null);
        SRIJKPlayManager.getInstance().stopAudio();
        XianShengSDK.getInstance().onDestroy();
    }
}
