package com.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.smartreader.ui.main.model.SRAddBookManager;
import com.smartreader.ui.main.model.SRMainModel;
import com.smartreader.ui.main.presenter.SRBookListPresenter;
import com.smartreader.ui.main.view.SRBookListFragment;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookListActivity extends ZYBaseFragmentActivity<SRBookListFragment> {

    static final String GRADE_ID = "grade_id";

    public static Intent createIntent(Context context, String grade_id) {
        Intent intent = new Intent(context, SRBookListActivity.class);
        intent.putExtra(GRADE_ID, grade_id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.showTitle("添加课程");
        SRAddBookManager.getInstance().clearAddBooks();
        new SRBookListPresenter(mFragment, new SRMainModel(), getIntent().getStringExtra(GRADE_ID));
    }

    @Override
    protected SRBookListFragment createFragment() {
        return new SRBookListFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SRAddBookManager.getInstance().clearAddBooks();
    }
}
