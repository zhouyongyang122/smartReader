package com.qudiandu.smartreader.ui.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.set.model.SRSetModel;
import com.qudiandu.smartreader.ui.set.presenter.SRSysMsgPresenter;
import com.qudiandu.smartreader.ui.set.view.SRSysMsgFragment;

/**
 * Created by ZY on 17/4/9.
 */

public class SRSysMsgActivity extends ZYBaseFragmentActivity<SRSysMsgFragment> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SRSysMsgPresenter(mFragment, new SRSetModel());
        mActionBar.showTitle("消息");
    }

    @Override
    protected SRSysMsgFragment createFragment() {
        return new SRSysMsgFragment();
    }
}
