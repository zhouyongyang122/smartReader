package com.qudiandu.smartdub.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartdub.base.event.SREventSelectedBook;
import com.qudiandu.smartdub.base.event.SREventSelectedTask;
import com.qudiandu.smartdub.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartdub.ui.main.model.SRGradeModel;
import com.qudiandu.smartdub.ui.main.presenter.SRGradePresenter;
import com.qudiandu.smartdub.ui.main.view.SRGradeFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ZY on 17/3/31.
 */

public class SRGradeActivity extends ZYBaseFragmentActivity<SRGradeFragment> {

    static final String TASK_SELECTE = "task_select";

    public static Intent createIntent(Context context) {
        return new Intent(context, SRGradeActivity.class);
    }

    public static Intent createIntent(Context context, boolean isTaskSelect) {
        Intent intent = new Intent(context, SRGradeActivity.class);
        intent.putExtra(TASK_SELECTE, isTaskSelect);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.showTitle("添加课程");
        new SRGradePresenter(mFragment,getIntent().getBooleanExtra(TASK_SELECTE,false));
    }

    @Override
    protected SRGradeFragment createFragment() {
        return new SRGradeFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBookSelected(SREventSelectedBook eventSelectedBook) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SREventSelectedTask task) {
        finish();
    }
}
