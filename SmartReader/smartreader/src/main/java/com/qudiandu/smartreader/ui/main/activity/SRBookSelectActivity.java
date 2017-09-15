package com.qudiandu.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.event.SREventSelectedTask;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.main.model.SRBookSelectManager;
import com.qudiandu.smartreader.ui.main.presenter.SRBookSelectPresenter;
import com.qudiandu.smartreader.ui.main.view.SRBookSelectFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookSelectActivity extends ZYBaseFragmentActivity<SRBookSelectFragment> {

    static final String GRADE_ID = "grade_id";

    static final String GRADE_NAME = "grade_name";

    static final String TASK_SELECTE = "task_select";

    public static Intent createIntent(Context context, String grade_id, String gradeName) {
        Intent intent = new Intent(context, SRBookSelectActivity.class);
        intent.putExtra(GRADE_ID, grade_id);
        intent.putExtra(GRADE_NAME, gradeName);
        return intent;
    }

    public static Intent createIntent(Context context, String grade_id, String gradeName, boolean isTaskSelect) {
        Intent intent = new Intent(context, SRBookSelectActivity.class);
        intent.putExtra(GRADE_ID, grade_id);
        intent.putExtra(TASK_SELECTE, isTaskSelect);
        intent.putExtra(GRADE_NAME, gradeName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.showTitle("添加课程");
        SRBookSelectManager.getInstance().clearAddBooks();
        new SRBookSelectPresenter(mFragment, getIntent().getStringExtra(GRADE_ID), getIntent().getBooleanExtra(TASK_SELECTE, false), getIntent().getStringExtra(GRADE_NAME));
    }

    @Override
    protected SRBookSelectFragment createFragment() {
        return new SRBookSelectFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SRBookSelectManager.getInstance().clearAddBooks();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SREventSelectedTask task) {
        finish();
    }
}
