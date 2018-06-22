package com.qudiandu.smartdub.ui.rank.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.adapter.ZYFragmentAdapter;
import com.qudiandu.smartdub.base.mvp.ZYBaseFragment;
import com.qudiandu.smartdub.ui.rank.model.SREventRefreshRankTop;
import com.qudiandu.smartdub.ui.rank.presenter.SRRankPresenter;
import com.qudiandu.smartdub.ui.rank.view.viewHolder.SRRankHeaderVH;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRankHomeFragment extends ZYBaseFragment {

    int timeType = SRRankPresenter.TIME_DAY_TYPE;

    @Bind(R.id.tvRightTitle)
    TextView tvRightTitle;

    @Bind(R.id.layoutHeader)
    LinearLayout layoutHeader;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPage)
    ViewPager viewPage;

    SRRankHeaderVH mHeaderVH;

    public SRRankPresenter schoolRankPresenter;

    public SRRankPresenter allRankPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_rank, container, false);
        ButterKnife.bind(this, view);
        ZYFragmentAdapter adapter = new ZYFragmentAdapter(getChildFragmentManager());

        SRRankFragment allRank = new SRRankFragment();
        allRankPresenter = new SRRankPresenter(allRank, SRRankPresenter.RANK_ALL_TYPE);
        adapter.addFragment(allRank, "全国排行");

        SRRankFragment schoolRank = new SRRankFragment();
        schoolRankPresenter = new SRRankPresenter(schoolRank, SRRankPresenter.RANK_CLASS_TYPE);
        adapter.addFragment(schoolRank, "班级排行");

        viewPage.setAdapter(adapter);

        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshRankTop();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPage);

        mHeaderVH = new SRRankHeaderVH();
        mHeaderVH.attachTo(layoutHeader);
        return view;
    }

    public void changeTimeRank(int timeType) {
        schoolRankPresenter.setTimeType(timeType);
        allRankPresenter.setTimeType(timeType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SREventRefreshRankTop event) {
        if (event != null) {
            int index = -1;
            if (event.type == SRRankPresenter.RANK_ALL_TYPE) {
                index = 0;
            } else if (event.type == SRRankPresenter.RANK_CLASS_TYPE) {
                index = 1;
            }

            if (viewPage.getCurrentItem() == index) {
                refreshRankTop();
            }
        }
    }

    public void refreshRankTop() {
        int position = viewPage.getCurrentItem();
        if (position == 0) {
            mHeaderVH.updateView(allRankPresenter.getTops(), allRankPresenter.isFristLoading());
        } else {
            mHeaderVH.updateView(schoolRankPresenter.getTops(), schoolRankPresenter.isFristLoading());
        }
    }

    @OnClick({R.id.tvRightTitle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRightTitle: {
                switch (timeType) {
                    case SRRankPresenter.TIME_DAY_TYPE:
                        tvRightTitle.setText("本周排行");
                        timeType = SRRankPresenter.TIME_WEEK_TYPE;
                        break;
                    case SRRankPresenter.TIME_WEEK_TYPE:
                        tvRightTitle.setText("今日排行");
                        timeType = SRRankPresenter.TIME_DAY_TYPE;
                        break;
                }
                changeTimeRank(timeType);
            }
            break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
