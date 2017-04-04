package com.smartreader.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.smartreader.ui.login.presenter.SRLoginPresenter;
import com.smartreader.ui.login.view.SRLoginFragment;

/**
 * Created by ZY on 17/4/4.
 */

public class SRLoginActivity extends ZYBaseFragmentActivity<SRLoginFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, SRLoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.showTitle("登录");

        new SRLoginPresenter(mFragment);
    }

    @Override
    protected SRLoginFragment createFragment() {
        return new SRLoginFragment();
    }
}
