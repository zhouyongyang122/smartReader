package com.qudiandu.smartreader.ui.myAudio.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.myAudio.model.SRMyAudioModel;
import com.qudiandu.smartreader.ui.myAudio.presenter.SRCataloguesPresenter;
import com.qudiandu.smartreader.ui.myAudio.view.SRCataloguesFragment;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCataloguesActivity extends ZYBaseFragmentActivity<SRCataloguesFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, SRCataloguesActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("我的作品");
        new SRCataloguesPresenter(mFragment, new SRMyAudioModel());
    }

    @Override
    protected SRCataloguesFragment createFragment() {
        return new SRCataloguesFragment();
    }
}
