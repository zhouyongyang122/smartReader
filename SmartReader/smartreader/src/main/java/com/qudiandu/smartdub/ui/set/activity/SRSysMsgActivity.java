package com.qudiandu.smartdub.ui.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartdub.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartdub.ui.set.model.SRSetModel;
import com.qudiandu.smartdub.ui.set.presenter.SRSysMsgPresenter;
import com.qudiandu.smartdub.ui.set.view.SRSysMsgFragment;

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
