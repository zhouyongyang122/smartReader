package com.smartreader.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.smartreader.ui.login.presenter.SRRegisterPresenter;
import com.smartreader.ui.login.view.SRRegisterFragment;

/**
 * Created by ZY on 17/4/4.
 */

public class SRRegisterActivity extends ZYBaseFragmentActivity<SRRegisterFragment> {
    static final String TYPE = "type";

    public static Intent createIntent(Context context, int type) {
        Intent intent = new Intent(context, SRRegisterActivity.class);
        intent.putExtra(TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra(TYPE, 0);
        switch (type) {
            case SRRegisterPresenter.REGISTER_TYPE:
                mActionBar.showTitle("注册账号");
                break;
            case SRRegisterPresenter.FORGET_TYPE:
                mActionBar.showTitle("重置密码");
                break;
            case SRRegisterPresenter.BIND_TYPE:
                mActionBar.showTitle("绑定手机号码");
                break;
            case SRRegisterPresenter.CHANGE_PWD_TYPE:
                mActionBar.showTitle("修改密码");
                break;
        }

        new SRRegisterPresenter(mFragment, type);
    }

    @Override
    protected SRRegisterFragment createFragment() {
        return new SRRegisterFragment();
    }
}
