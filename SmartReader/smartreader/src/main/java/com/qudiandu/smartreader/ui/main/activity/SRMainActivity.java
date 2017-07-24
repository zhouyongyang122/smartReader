package com.qudiandu.smartreader.ui.main.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.service.ZYUpdateService;
import com.qudiandu.smartreader.thirdParty.xiansheng.XianShengSDK;
import com.qudiandu.smartreader.ui.main.contract.SRMainContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRVersion;
import com.qudiandu.smartreader.ui.main.presenter.SRClassPresenter;
import com.qudiandu.smartreader.ui.main.presenter.SRHomePresenter;
import com.qudiandu.smartreader.ui.main.presenter.SRMainPresenter;
import com.qudiandu.smartreader.ui.main.view.SRClassFragment;
import com.qudiandu.smartreader.ui.main.view.SRHomeFragment;
import com.qudiandu.smartreader.ui.main.view.SRMeFragment;
import com.qudiandu.smartreader.base.adapter.ZYFragmentAdapter;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.utils.ZYStatusBarUtils;
import com.qudiandu.smartreader.utils.ZYToast;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/15.
 */

public class SRMainActivity extends ZYBaseActivity<SRMainContract.IPresenter> implements SRMainContract.IView {

    public static final int MAIN_HOME_INDEX = 0;

    public static final int MAIN_CLASS_INDEX = 1;

    public static final int MAIN_ME_INDEX = 2;

    @Bind(R.id.mainViewPager)
    ViewPager mainViewPager;

    @Bind(R.id.homeImg)
    ImageView homeImg;

    @Bind(R.id.homeName)
    TextView homeName;

    @Bind(R.id.classImg)
    ImageView classImg;

    @Bind(R.id.className)
    TextView className;

    @Bind(R.id.meImg)
    ImageView meImg;

    @Bind(R.id.meName)
    TextView meName;

    SRHomeFragment homeFragment;

    SRClassFragment classFragment;

    SRMeFragment meFragment;

    ZYFragmentAdapter fragmentAdapter;

    private int mCurrentPage = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_main);
        new SRMainPresenter(this);
        initView();
        ZYStatusBarUtils.immersiveStatusBar(this, 1);
        if (ZYStatusBarUtils.isCanLightStatusBar()) {
            ZYStatusBarUtils.tintStatusBar(this, Color.TRANSPARENT, 0);
        }

        XianShengSDK.getInstance().init(this);
    }

    private void initView() {
        hideActionLeftImg();
        fragmentAdapter = new ZYFragmentAdapter(getSupportFragmentManager());
        homeFragment = new SRHomeFragment();
        classFragment = new SRClassFragment();
        meFragment = new SRMeFragment();
        fragmentAdapter.addFragment(homeFragment, "英语趣点读");
        fragmentAdapter.addFragment(classFragment, "班级");
        fragmentAdapter.addFragment(meFragment, "我的");

        new SRHomePresenter(homeFragment);
        new SRClassPresenter(classFragment);

        mainViewPager.setOffscreenPageLimit(2);
        mainViewPager.setCurrentItem(0);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mainViewPager.setAdapter(fragmentAdapter);
        changeFragment(0);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mActionBar.getLayoutParams();
        layoutParams.height = layoutParams.height + ZYStatusBarUtils.getStatusBarHeight(this);
        mActionBar.setPadding(0, ZYStatusBarUtils.getStatusBarHeight(this), 0, 0);
        mActionBar.setLayoutParams(layoutParams);
    }

    @OnClick({R.id.homeBtn, R.id.classBtn, R.id.meBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeBtn:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.classBtn:
                mainViewPager.setCurrentItem(1);
                break;
            case R.id.meBtn:
                mainViewPager.setCurrentItem(2);
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
            homeName.setTextColor(getResources().getColor(R.color.white));
            meName.setTextColor(getResources().getColor(R.color.black));
            classImg.setSelected(false);
            className.setTextColor(getResources().getColor(R.color.black));
            showActionBar();
            setDarkMode(false);
        } else if (mCurrentPage == MAIN_CLASS_INDEX) {
            homeImg.setSelected(false);
            meImg.setSelected(false);
            homeName.setTextColor(getResources().getColor(R.color.black));
            meName.setTextColor(getResources().getColor(R.color.black));
            classImg.setSelected(true);
            className.setTextColor(getResources().getColor(R.color.white));
            hideActionBar();
            setDarkMode(false);
        } else if (mCurrentPage == MAIN_ME_INDEX) {
            homeImg.setSelected(false);
            meImg.setSelected(true);
            classImg.setSelected(false);
            className.setTextColor(getResources().getColor(R.color.black));
            homeName.setTextColor(getResources().getColor(R.color.black));
            meName.setTextColor(getResources().getColor(R.color.white));
            hideActionBar();
            setDarkMode(true);
        }
        showTitle(fragmentAdapter.getPageTitle(position).toString());
    }

    @Override
    public void showUpdateView(final SRVersion version) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("版本更新");
        dialog.setMessage(version.info);
        dialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ZYToast.show(SRMainActivity.this, "正在下载中...");
                startService(ZYUpdateService.createIntent(version.download));
            }
        });
        if (version.keyupdate > 0) {
            //强更
            dialog.setCancelable(false);
        } else {
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getVersion();
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
    public void onBackPressed() {

        new AlertDialog.Builder(this).setTitle("退出").setMessage("是否退出英语趣点读?")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MobclickAgent.onKillProcess(SRMainActivity.this);
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("再看看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }
}
