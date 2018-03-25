package com.qudiandu.smartreader.ui.rank.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYFragmentAdapter;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.ui.rank.presenter.SRRankPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRankHomeFragment extends ZYBaseFragment {

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPage)
    ViewPager viewPage;

    public SRRankPresenter schoolRankPresenter;

    public SRRankPresenter allRankPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_rank, container, false);
        ButterKnife.bind(this,view);

        ZYFragmentAdapter adapter = new ZYFragmentAdapter(getChildFragmentManager());

        SRRankFragment allRank = new SRRankFragment();
        allRankPresenter = new SRRankPresenter(allRank, SRRankPresenter.RANK_ALL_TYPE);
        adapter.addFragment(allRank, "全国排行");

        SRRankFragment schoolRank = new SRRankFragment();
        schoolRankPresenter = new SRRankPresenter(schoolRank, SRRankPresenter.RANK_CLASS_TYPE);
        adapter.addFragment(schoolRank, "班级排行");

        viewPage.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPage);
        return view;
    }

    public void changeTimeRank(int timeType) {
        schoolRankPresenter.setTimeType(timeType);
        allRankPresenter.setTimeType(timeType);
    }
}
