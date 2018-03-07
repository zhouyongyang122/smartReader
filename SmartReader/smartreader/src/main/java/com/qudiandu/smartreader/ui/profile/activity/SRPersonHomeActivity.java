package com.qudiandu.smartreader.ui.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.profile.presenter.SRPersonHomePresenter;
import com.qudiandu.smartreader.ui.profile.view.SRPersonHomeFragment;

/**
 * Created by ZY on 18/3/7.
 */

public class SRPersonHomeActivity extends ZYBaseFragmentActivity<SRPersonHomeFragment> {

    public static Intent createIntent(Context context, SRUser user) {
        Intent intent = new Intent(context, SRPersonHomeActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    public static Intent createIntent(Context context, String uid) {
        Intent intent = new Intent(context, SRPersonHomeActivity.class);
        intent.putExtra("uid", uid);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getSerializableExtra("user") != null) {
            new SRPersonHomePresenter(mFragment, (SRUser) getIntent().getSerializableExtra("user"));
        } else {
            new SRPersonHomePresenter(mFragment, getIntent().getStringExtra("uid"));
        }

    }

    @Override
    protected SRPersonHomeFragment createFragment() {
        return new SRPersonHomeFragment();
    }
}