package com.qudiandu.smartreader.ui.wordStudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.wordStudy.presenter.SRWordStudyUnitsPresenter;
import com.qudiandu.smartreader.ui.wordStudy.view.SRWordStudyUnitsFragment;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyUnitsActivity extends ZYBaseFragmentActivity<SRWordStudyUnitsFragment> {

    public static Intent createIntent(Context context, SRBook book) {
        Intent intent = new Intent(context, SRWordStudyUnitsActivity.class);
        intent.putExtra("book", book);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SRWordStudyUnitsPresenter(mFragment, (SRBook) getIntent().getSerializableExtra("book"));
    }

    @Override
    protected SRWordStudyUnitsFragment createFragment() {
        return new SRWordStudyUnitsFragment();
    }
}
