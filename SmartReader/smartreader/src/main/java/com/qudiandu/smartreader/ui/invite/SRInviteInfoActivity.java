package com.qudiandu.smartreader.ui.invite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 上午10:59$
 */
public class SRInviteInfoActivity extends ZYBaseFragmentActivity<SRInviteInfoFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, SRInviteInfoActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("邀请明细");
        new SRInviteInfoPresenter(mFragment);
    }

    @Override
    protected SRInviteInfoFragment createFragment() {
        return new SRInviteInfoFragment();
    }
}
