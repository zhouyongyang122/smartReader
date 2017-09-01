package com.qudiandu.smartreader.ui.myAudio.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.myAudio.presenter.SRCatalogueDetailPresenter;
import com.qudiandu.smartreader.ui.myAudio.view.SRCatalogueDetailFragment;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCatalogueDetailActivity extends ZYBaseFragmentActivity<SRCatalogueDetailFragment> {

    public static Intent createIntent(Context context, String show_id) {
        Intent intent = new Intent(context, SRCatalogueDetailActivity.class);
        intent.putExtra("show_id", show_id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        new SRCatalogueDetailPresenter(mFragment, getIntent().getStringExtra("show_id"));
    }

    @Override
    protected boolean tintStatusBar() {
        return false;
    }

    @Override
    protected SRCatalogueDetailFragment createFragment() {
        return new SRCatalogueDetailFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SRPlayManager.getInstance().setPagePlayListener(null);
        SRPlayManager.getInstance().stopAudio();
    }
}
