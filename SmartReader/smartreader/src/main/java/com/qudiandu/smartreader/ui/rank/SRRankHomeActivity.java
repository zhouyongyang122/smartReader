package com.qudiandu.smartreader.ui.rank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.rank.presenter.SRRankPresenter;
import com.qudiandu.smartreader.ui.rank.view.SRRankHomeFragment;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRankHomeActivity extends ZYBaseFragmentActivity<SRRankHomeFragment> implements View.OnClickListener {

    int timeType = SRRankPresenter.TIME_DAY_TYPE;

    public static Intent createIntent(Context context) {
        return new Intent(context, SRRankHomeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.showTitle("排行榜");
        mActionBar.showActionRightTitle("今日排行", this);
    }

    @Override
    public void onClick(View v) {
        switch (timeType) {
            case SRRankPresenter.TIME_DAY_TYPE:
                mActionBar.showActionRightTitle("本周排行", this);
                timeType = SRRankPresenter.TIME_WEEK_TYPE;
                break;
            case SRRankPresenter.TIME_WEEK_TYPE:
                mActionBar.showActionRightTitle("今日排行", this);
                timeType = SRRankPresenter.TIME_DAY_TYPE;
                break;
        }
        mFragment.changeTimeRank(timeType);
    }

    @Override
    protected SRRankHomeFragment createFragment() {
        return new SRRankHomeFragment();
    }
}
