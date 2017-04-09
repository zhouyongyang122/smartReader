package com.smartreader.ui.mark.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.smartreader.ui.main.model.SRPageManager;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.mark.presenter.SRMarkPresenter;
import com.smartreader.ui.mark.view.SRMarkFragment;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkActivity extends ZYBaseFragmentActivity<SRMarkFragment> {

    static final String PAGE = "page";
    static final String BOOK_ID = "book_id";

    public static Intent createIntent(Context context, SRPage page, String bookId) {
        Intent intent = new Intent(context, SRMarkActivity.class);
        intent.putExtra(PAGE, page);
        intent.putExtra(BOOK_ID, bookId);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SRPage page = (SRPage) getIntent().getSerializableExtra(PAGE);
        String bookId = getIntent().getStringExtra(BOOK_ID);
        mActionBar.showTitle("练口语");
        new SRMarkPresenter(mFragment, page, bookId);
    }

    @Override
    public void onBackPressed() {
        if (!mFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected SRMarkFragment createFragment() {
        return new SRMarkFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SRPageManager.getInstance().stopAudio();
    }
}
