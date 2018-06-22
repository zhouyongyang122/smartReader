package com.qudiandu.smartdub.ui.book.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartdub.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartdub.ui.book.presenter.SRBookUnitsPresenter;
import com.qudiandu.smartdub.ui.book.view.SRBookUnitsFragment;
import com.qudiandu.smartdub.ui.main.activity.SRBookHomeActivity;

/**
 * Created by ZY on 17/9/7.
 */

public class SRBookUnitsActivity extends ZYBaseFragmentActivity<SRBookUnitsFragment> {

    static final String LOCAL_PATH = "localPath";

    static final String TITLE = "title";

    public static Intent createIntent(Context context, String localPath, String title) {
        Intent intent = new Intent(context, SRBookUnitsActivity.class);
        intent.putExtra(LOCAL_PATH, localPath);
        intent.putExtra(TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle(getIntent().getStringExtra(TITLE));
        String localPath = getIntent().getStringExtra(LOCAL_PATH);
        new SRBookUnitsPresenter(mFragment, localPath);
    }

    @Override
    protected SRBookUnitsFragment createFragment() {
        return new SRBookUnitsFragment();
    }
}
