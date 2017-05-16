package com.qudiandu.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.event.SREventSelectedBook;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.main.model.SRGradeModel;
import com.qudiandu.smartreader.ui.main.presenter.SRGradePresenter;
import com.qudiandu.smartreader.ui.main.view.SRGradeFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ZY on 17/3/31.
 */

public class SRGradeActivity extends ZYBaseFragmentActivity<SRGradeFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, SRGradeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.showTitle("添加课程");
        new SRGradePresenter(mFragment, new SRGradeModel());
    }

    @Override
    protected SRGradeFragment createFragment() {
        return new SRGradeFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBookSelected(SREventSelectedBook eventSelectedBook) {
        finish();
    }
}
