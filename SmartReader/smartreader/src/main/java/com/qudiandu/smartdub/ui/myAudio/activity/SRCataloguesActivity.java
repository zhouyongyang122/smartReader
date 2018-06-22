package com.qudiandu.smartdub.ui.myAudio.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.qudiandu.smartdub.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartdub.ui.myAudio.model.SRMyAudioModel;
import com.qudiandu.smartdub.ui.myAudio.presenter.SRCataloguesPresenter;
import com.qudiandu.smartdub.ui.myAudio.view.SRCataloguesFragment;

import butterknife.OnClick;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCataloguesActivity extends ZYBaseFragmentActivity<SRCataloguesFragment> implements View.OnClickListener {

    boolean isEditing;

    public static Intent createIntent(Context context) {
        return new Intent(context, SRCataloguesActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("我的作品");
        new SRCataloguesPresenter(mFragment, new SRMyAudioModel());

        showActionRightTitle("编辑", this);
    }

    @Override
    public void onClick(View v) {
        if (!isEditing) {
            isEditing = true;
            showActionRightTitle("取消", this);
            mFragment.setEdit(true);
        } else {
            isEditing = false;
            showActionRightTitle("编辑", this);
            mFragment.setEdit(false);
        }
    }

    @Override
    protected SRCataloguesFragment createFragment() {
        return new SRCataloguesFragment();
    }
}
