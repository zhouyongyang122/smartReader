package com.qudiandu.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.main.presenter.SRClassDetailPresenter;
import com.qudiandu.smartreader.ui.main.view.SRClassDetailFragment;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailActivity extends ZYBaseFragmentActivity<SRClassDetailFragment> {

    static final String GROUP_ID = "group_id";

    public static Intent createIntent(Context context, String group_id) {
        Intent intent = new Intent(context, SRClassDetailActivity.class);
        intent.putExtra(GROUP_ID, group_id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("班级详情");
        new SRClassDetailPresenter(mFragment, getIntent().getStringExtra(GROUP_ID));
    }

    @Override
    protected SRClassDetailFragment createFragment() {
        return new SRClassDetailFragment();
    }
}
