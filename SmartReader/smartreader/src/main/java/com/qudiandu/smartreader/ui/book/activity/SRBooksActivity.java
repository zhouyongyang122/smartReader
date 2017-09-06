package com.qudiandu.smartreader.ui.book.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.book.presenter.SRBooksPresenter;
import com.qudiandu.smartreader.ui.book.view.SRBooksFragment;

/**
 * Created by ZY on 17/8/31.
 */

public class SRBooksActivity extends ZYBaseFragmentActivity<SRBooksFragment> {

    static final String CLASS_ID = "classId";

    public static Intent createIntent(Context context, int classId) {
        Intent intent = new Intent(context, SRBooksActivity.class);
        intent.putExtra(CLASS_ID, classId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("我的教材");
        new SRBooksPresenter(mFragment, getIntent().getIntExtra(CLASS_ID, 0));
    }

    @Override
    protected SRBooksFragment createFragment() {
        return new SRBooksFragment();
    }
}
