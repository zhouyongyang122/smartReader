package com.qudiandu.smartreader.ui.dubbing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.ui.dubbing.presenter.SRDubbingPresenter;
import com.qudiandu.smartreader.ui.dubbing.view.SRDubbingFragment;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/12/1.
 */

public class SRDubbingActivity extends ZYBaseActivity {

    static final String TRACTS = "tracts";
    static final String BOOK_ID = "book_id";
    static final String CATALOGUE_ID = "catalogue_id";
    static final String GROUP_ID = "group_id";
    static final String TASK_DI = "task_id";
    static final String TITLE = "title";

    public static Intent createIntent(Context context
            , ArrayList<SRTract> tracts
            , String bookId
            , String catalogue_id
            , String title) {
        Intent intent = new Intent(context, SRDubbingActivity.class);
        intent.putExtra(TRACTS, tracts);
        intent.putExtra(BOOK_ID, bookId);
        intent.putExtra(CATALOGUE_ID, catalogue_id);
        intent.putExtra(TITLE, title);
        return intent;
    }

    public static Intent createIntent(Context context
            , ArrayList<SRTract> tracts
            , String bookId
            , String catalogue_id
            , String group_id
            , String task_id
            , String title) {
        Intent intent = new Intent(context, SRDubbingActivity.class);
        intent.putExtra(TRACTS, tracts);
        intent.putExtra(BOOK_ID, bookId);
        intent.putExtra(CATALOGUE_ID, catalogue_id);
        intent.putExtra(GROUP_ID, group_id);
        intent.putExtra(TASK_DI, task_id);
        intent.putExtra(TITLE, title);
        return intent;
    }

    @Bind(R.id.viewPage)
    ViewPager mViewPage;

    @Bind(R.id.textPage)
    TextView mTextPage;

    DubbingAdapter mAdapter;

    List<SRDubbingFragment> mFragments = new ArrayList<>();

    ArrayList<SRTract> mTracts;
    String mBookId;
    String mCatalogueId;
    String mGroupId;
    String mTaskId;
    String mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_dubbing);

        mTracts = (ArrayList<SRTract>) getIntent().getSerializableExtra(TRACTS);
        mBookId = getIntent().getStringExtra(BOOK_ID);
        mCatalogueId = getIntent().getStringExtra(CATALOGUE_ID);
        mGroupId = getIntent().getStringExtra(GROUP_ID);
        mTaskId = getIntent().getStringExtra(TASK_DI);
        mTitle = getIntent().getStringExtra(TITLE);

        mActionBar.showTitle(mTitle);
        SRDubbingFragment fragment;
        int pageIndex = 0;
        for (SRTract tract : mTracts) {
            fragment = new SRDubbingFragment();
            new SRDubbingPresenter(fragment, tract, mCatalogueId, mTaskId, mGroupId, pageIndex);
            mFragments.add(fragment);
            pageIndex++;
        }

        int width = ZYScreenUtils.getScreenWidth(this) - ZYScreenUtils.dp2px(this, 60);
        float scale = 315.0f / 493.0f;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewPage.getLayoutParams();
        layoutParams.height = (int) (width / scale);
        mViewPage.setLayoutParams(layoutParams);
        mAdapter = new DubbingAdapter(getSupportFragmentManager());
        mViewPage.setAdapter(mAdapter);
        mViewPage.setPageTransformer(false, new ScaleTransformer());
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTextPage.setText((position + 1) + "/" + mFragments.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.layoutSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutSubmit:
                break;
        }
    }

    private class DubbingAdapter extends FragmentStatePagerAdapter {

        public DubbingAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public View getViewAt(int position) {
            return mFragments.get(position).getRootView();
        }
    }

    private class ScaleTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

        private float mLastOffset;

        public ScaleTransformer() {
            mViewPage.addOnPageChangeListener(this);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realCurrentPosition;
            int nextPosition;
            float realOffset;
            boolean goingLeft = mLastOffset > positionOffset;

            if (goingLeft) {
                realCurrentPosition = position + 1;
                nextPosition = position;
                realOffset = 1 - positionOffset;
            } else {
                nextPosition = position + 1;
                realCurrentPosition = position;
                realOffset = positionOffset;
            }

            if (realCurrentPosition > mAdapter.getCount() - 1) {
                realCurrentPosition = mAdapter.getCount() - 1;
            }
            View currentView = mAdapter.getViewAt(realCurrentPosition);
            if (currentView != null) {
                currentView.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                currentView.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }

            if (nextPosition > mAdapter.getCount() - 1) {
                return;
            }

            View nextView = mAdapter.getViewAt(nextPosition);
            if (nextView != null) {
                nextView.setScaleX((float) (1 + 0.1 * realOffset));
                nextView.setScaleY((float) (1 + 0.1 * realOffset));
            }

            mLastOffset = positionOffset;
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void transformPage(View page, float position) {

        }
    }
}
