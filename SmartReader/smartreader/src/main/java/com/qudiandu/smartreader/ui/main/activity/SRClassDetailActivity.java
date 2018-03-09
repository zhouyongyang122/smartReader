package com.qudiandu.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.presenter.SRClassDetailPresenter;
import com.qudiandu.smartreader.ui.main.view.SRClassDetailFragment;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailActivity extends ZYBaseFragmentActivity<SRClassDetailFragment> implements View.OnClickListener {

    static final String CLASS = "class";

    SRClass mClass;

    SRClassDetailPresenter mPresenter;

    boolean mIsEdit;

    public static Intent createIntent(Context context, SRClass srClass) {
        Intent intent = new Intent(context, SRClassDetailActivity.class);
        intent.putExtra(CLASS, srClass);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClass = (SRClass) getIntent().getSerializableExtra(CLASS);
        showTitle(mClass.class_name + "(" + mClass.cur_num + ")");
        if (SRUserManager.getInstance().getUser().uid.equals(mClass.uid + "")) {
            showActionRightTitle("编辑", this);
        }
        mPresenter = new SRClassDetailPresenter(mFragment, mClass);
    }

    @Override
    public void onClick(View v) {
        mIsEdit = !mIsEdit;
        showActionRightTitle(mIsEdit ? "取消" : "编辑", this);
        mFragment.edit(mIsEdit);
    }

    @Override
    protected SRClassDetailFragment createFragment() {
        return new SRClassDetailFragment();
    }
}
