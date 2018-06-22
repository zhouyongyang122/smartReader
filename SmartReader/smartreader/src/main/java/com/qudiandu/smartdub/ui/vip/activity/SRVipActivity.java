package com.qudiandu.smartdub.ui.vip.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartdub.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.vip.presenter.SRVipPresenter;
import com.qudiandu.smartdub.ui.vip.view.SRVipFragment;

/**
 * Created by ZY on 18/3/1.
 */

public class SRVipActivity extends ZYBaseFragmentActivity<SRVipFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, SRVipActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SRUserManager.getInstance().isGuesterUser(true)) {
            finish();
            return;
        }

        mActionBar.showTitle("会员中心");

        new SRVipPresenter(mFragment);
    }

    @Override
    protected SRVipFragment createFragment() {
        return new SRVipFragment();
    }
}
