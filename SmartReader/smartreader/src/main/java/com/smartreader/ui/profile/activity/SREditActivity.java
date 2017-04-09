package com.smartreader.ui.profile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartreader.base.mvp.ZYBaseActivity;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.smartreader.ui.profile.presenter.SREditPresenter;
import com.smartreader.ui.profile.view.SREditFragment;

/**
 * Created by ZY on 17/4/7.
 */

public class SREditActivity extends ZYBaseFragmentActivity<SREditFragment> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.showTitle("我的信息");
        new SREditPresenter(mFragment);
    }

    @Override
    protected SREditFragment createFragment() {
        return new SREditFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment.onActivityResult(requestCode, resultCode, data);
    }
}
