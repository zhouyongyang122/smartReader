package com.qudiandu.smartreader.ui.main.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.service.ZYUpdateService;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadManager;
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
import com.qudiandu.smartreader.ui.rank.presenter.SRRankPresenter;
import com.qudiandu.smartreader.ui.rank.view.SRRankHomeFragment;
import com.qudiandu.smartreader.utils.ZYStatusBarUtils;
import com.qudiandu.smartreader.utils.ZYToast;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

/**
 * Created by ZY on 17/3/15.
 */

public class SRMainActivity extends ZYBaseActivity<SRMainContract.IPresenter> implements SRMainContract.IView {

    public static final int MAIN_HOME_INDEX = 0;

    public static final int MAIN_CLASS_INDEX = 1;

    public static final int MAIN_RANK_INDEX = 2;

    public static final int MAIN_ME_INDEX = 3;

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

    @Bind(R.id.rankImg)
    ImageView rankImg;

    @Bind(R.id.rankName)
    TextView rankName;

    @Bind(R.id.meImg)
    ImageView meImg;

    @Bind(R.id.meName)
    TextView meName;

    SRHomeFragment homeFragment;

    SRClassFragment classFragment;

    SRMeFragment meFragment;

    SRRankHomeFragment mRankHomeFragment;

    ZYFragmentAdapter fragmentAdapter;

    private int mCurrentPage = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_main);
        hideActionBar();
        new SRMainPresenter(this);
        initView();
        XianShengSDK.getInstance().init(this);
        SQLiteStudioService.instance().start(this);
        mPresenter.uploadJpushId();
    }

    private void initView() {
        hideActionLeftImg();
        fragmentAdapter = new ZYFragmentAdapter(getSupportFragmentManager());
        homeFragment = new SRHomeFragment();
        classFragment = new SRClassFragment();
        mRankHomeFragment = new SRRankHomeFragment();

        meFragment = new SRMeFragment();
        fragmentAdapter.addFragment(homeFragment, "英语趣点读");
        fragmentAdapter.addFragment(classFragment, "班级");
        fragmentAdapter.addFragment(mRankHomeFragment, "排行");
        fragmentAdapter.addFragment(meFragment, "我的");

        new SRHomePresenter(homeFragment);
        new SRClassPresenter(classFragment);

        mainViewPager.setOffscreenPageLimit(4);
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

    @OnClick({R.id.homeBtn, R.id.classBtn, R.id.meBtn, R.id.rankBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeBtn:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.classBtn:
                mainViewPager.setCurrentItem(1);
                break;
            case R.id.rankBtn:
                mainViewPager.setCurrentItem(2);
                break;
            case R.id.meBtn:
                mainViewPager.setCurrentItem(3);
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
            classImg.setSelected(false);
            rankImg.setSelected(false);
            classFragment.cancleManager();
            homeFragment.refreshMsgRemind();
        } else if (mCurrentPage == MAIN_CLASS_INDEX) {
            classImg.setSelected(true);
            homeImg.setSelected(false);
            rankImg.setSelected(false);
            meImg.setSelected(false);
        } else if (mCurrentPage == MAIN_RANK_INDEX) {
            meImg.setSelected(false);
            homeImg.setSelected(false);
            classImg.setSelected(false);
            rankImg.setSelected(true);
            classFragment.cancleManager();
        } else if (mCurrentPage == MAIN_ME_INDEX) {
            meImg.setSelected(true);
            homeImg.setSelected(false);
            classImg.setSelected(false);
            rankImg.setSelected(false);
            classFragment.cancleManager();
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
        mPresenter.msgRemind();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLiteStudioService.instance().stop();
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

        if (!classFragment.onBackPressed()) {
            return;
        }

        new AlertDialog.Builder(this).setTitle("退出").setMessage("是否退出英语趣点读?")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MobclickAgent.onKillProcess(SRMainActivity.this);
                        ZYDownloadManager.getInstance().cancleAll();
                        ZYDownloadManager.getInstance().stopSer();
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
