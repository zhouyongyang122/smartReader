package com.qudiandu.smartreader.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.task.presenter.SRTaskCatePresenter;
import com.qudiandu.smartreader.ui.task.view.SRTaskCateFragment;

/**
 * Created by ZY on 17/7/23.
 */

public class SRTaskCateActivity extends ZYBaseFragmentActivity<SRTaskCateFragment> {

    SRTaskCatePresenter mPresenter;

    public static Intent createIntent(Context context, String bookId) {
        Intent intent = new Intent(context, SRTaskCateActivity.class);
        intent.putExtra("bookId", bookId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("选择课时");
        mPresenter = new SRTaskCatePresenter(mFragment, getIntent().getStringExtra("bookId"));

        showActionRightTitle("确定布置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addTask();
            }
        });
    }

    @Override
    protected SRTaskCateFragment createFragment() {
        return new SRTaskCateFragment();
    }
}
