package com.smartreader.business.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.business.main.contract.SRMainContract;
import com.smartreader.business.main.presenter.SRMainPresenter;
import com.smartreader.business.main.view.SRHomeFragment;
import com.smartreader.business.main.view.SRMeFragment;
import com.smartreader.common.adapter.SRFragmentAdapter;
import com.smartreader.common.mvp.SRBaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/15.
 */

public class SRMainActivity extends SRBaseActivity<SRMainContract.IPresenter> implements SRMainContract.IView {

    public static final int MAIN_HOME_INDEX = 0;

    public static final int MAIN_ME_INDEX = 1;

    @Bind(R.id.mainViewPager)
    ViewPager mainViewPager;

    @Bind(R.id.homeImg)
    ImageView homeImg;

    @Bind(R.id.homeName)
    TextView homeName;

    @Bind(R.id.meImg)
    ImageView meImg;

    @Bind(R.id.meName)
    TextView meName;

    SRHomeFragment homeFragment;

    SRMeFragment meFragment;

    SRFragmentAdapter fragmentAdapter;

    private int mCurrentPage = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_main);
        new SRMainPresenter(this);
        initView();
    }

    private void initView() {
        hideActionLeftImg();
        fragmentAdapter = new SRFragmentAdapter(getSupportFragmentManager());
        homeFragment = new SRHomeFragment();
        meFragment = new SRMeFragment();
        fragmentAdapter.addFragment(homeFragment, "英语趣点读");
        fragmentAdapter.addFragment(meFragment, "我的");
        mainViewPager.setOffscreenPageLimit(1);
        mainViewPager.setCurrentItem(0);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeFragment(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mainViewPager.setAdapter(fragmentAdapter);
        changeFragment(0);
    }

    @OnClick({R.id.homeBtn, R.id.meBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeBtn:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.meBtn:
                mainViewPager.setCurrentItem(1);
                break;
        }
    }

    private void changeFragment(int position) {
        if (position == mCurrentPage) {
            return;
        }
        mCurrentPage = position;
        if (mCurrentPage == MAIN_HOME_INDEX) {
            homeImg.setSelected(true);
            meImg.setSelected(false);
            homeName.setTextColor(getResources().getColor(R.color.c1));
            meName.setTextColor(getResources().getColor(R.color.c5));
        } else if (mCurrentPage == MAIN_ME_INDEX) {
            homeImg.setSelected(false);
            meImg.setSelected(true);
            homeName.setTextColor(getResources().getColor(R.color.c5));
            meName.setTextColor(getResources().getColor(R.color.c1));
        }
        showTitle(fragmentAdapter.getPageTitle(position).toString());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void viewAction(Object message, String action) {

    }
}
