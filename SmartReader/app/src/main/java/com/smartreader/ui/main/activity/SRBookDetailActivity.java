package com.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.smartreader.ui.main.model.SRPageManager;
import com.smartreader.ui.main.presenter.SRBookDetailPresenter;
import com.smartreader.ui.main.view.SRBookDetailFragment;

/**
 * Created by ZY on 17/3/29.
 */

public class SRBookDetailActivity extends ZYBaseFragmentActivity<SRBookDetailFragment> {
    static final String LOCAL_PATH = "localPath";

    public static Intent createIntent(Context context, String localPath) {
        Intent intent = new Intent(context, SRBookDetailActivity.class);
        intent.putExtra(LOCAL_PATH, localPath);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();

        String localPath = getIntent().getStringExtra(LOCAL_PATH);

        new SRBookDetailPresenter(mFragment, localPath);
    }

    @Override
    protected SRBookDetailFragment createFragment() {
        return new SRBookDetailFragment();
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
        SRPageManager.getInstance().stopAudio();
    }
}
