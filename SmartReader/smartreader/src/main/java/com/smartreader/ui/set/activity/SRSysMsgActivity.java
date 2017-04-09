package com.smartreader.ui.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartreader.base.mvp.ZYBaseActivity;
import com.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.smartreader.ui.set.model.SRSetModel;
import com.smartreader.ui.set.presenter.SRSysMsgPresenter;
import com.smartreader.ui.set.view.SRSysMsgFragment;

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
