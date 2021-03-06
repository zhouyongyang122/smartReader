package com.qudiandu.smartreader.ui.mark.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.presenter.SRMarkPresenter;
import com.qudiandu.smartreader.ui.mark.view.SRMarkFragment;

import java.util.ArrayList;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkActivity extends ZYBaseFragmentActivity<SRMarkFragment> implements SRMarkFragment.OnTrackClickListener {

    static final String TRACTS = "tracts";
    static final String BOOK_ID = "book_id";
    static final String CATALOGUE_ID = "catalogue_id";
    static final String GROUP_ID = "group_id";
    static final String TASK_DI = "task_id";

    public static Intent createIntent(Context context, ArrayList<SRTract> tracts, String bookId, String catalogue_id) {
        Intent intent = new Intent(context, SRMarkActivity.class);
        intent.putExtra(TRACTS, tracts);
        intent.putExtra(BOOK_ID, bookId);
        intent.putExtra(CATALOGUE_ID, catalogue_id);
        return intent;
    }

    public static Intent createIntent(Context context, ArrayList<SRTract> tracts, String bookId, String catalogue_id, String group_id, String task_id) {
        Intent intent = new Intent(context, SRMarkActivity.class);
        intent.putExtra(TRACTS, tracts);
        intent.putExtra(BOOK_ID, bookId);
        intent.putExtra(CATALOGUE_ID, catalogue_id);
        intent.putExtra(GROUP_ID, group_id);
        intent.putExtra(TASK_DI, task_id);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<SRTract> tracts = (ArrayList<SRTract>) getIntent().getSerializableExtra(TRACTS);
        String bookId = getIntent().getStringExtra(BOOK_ID);
        String catalogue_id = getIntent().getStringExtra(CATALOGUE_ID);
        mActionBar.showTitle("(1/" + tracts.size() + ")");
        new SRMarkPresenter(mFragment, tracts, bookId, catalogue_id, getIntent().getStringExtra(GROUP_ID), getIntent().getStringExtra(TASK_DI));
        mFragment.setTrackClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (!mFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onTrackClick(int position, int size, SRTract tract) {
        mActionBar.showTitle("(" + (position + 1) + "/" + size + ")");
    }

    @Override
    protected SRMarkFragment createFragment() {
        return new SRMarkFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SRPlayManager.getInstance().stopAudio();
    }
}
