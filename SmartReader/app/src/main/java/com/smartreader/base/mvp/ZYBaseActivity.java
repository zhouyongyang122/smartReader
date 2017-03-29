package com.smartreader.base.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.smartreader.R;
import com.smartreader.SRApplication;
import com.smartreader.utils.ZYStatusBarUtils;
import com.smartreader.utils.ZYLog;
import com.smartreader.base.view.ZYActionBar;
import com.smartreader.base.view.ZYWaitDialog;
import com.smartreader.thirdParty.statistics.DataStatistics;

import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/13.
 */

public class ZYBaseActivity<P extends ZYIBasePresenter> extends AppCompatActivity implements ZYIBaseView<P> {

    protected ZYWaitDialog mWaitDialog;

    protected ZYActionBar mActionBar;

    protected RelativeLayout mRootView;

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ZYStatusBarUtils.isCanLightStatusBar() && tintStatusBar()) {
            setDarkMode(false);
            ZYStatusBarUtils.tintStatusBar(this, ContextCompat.getColor(this, getStatusColor()), 0);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mRootView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.sr_activity_base, null);
        mActionBar = (ZYActionBar) mRootView.findViewById(R.id.baseToolBar);
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.baseToolBar);
        view.setLayoutParams(layoutParams);
        mRootView.addView(view);

        mActionBar.mIvLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        super.setContentView(mRootView);

        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ZYLog.e(getClass().getSimpleName(), "onResume");
        DataStatistics.onResume(this);
        SRApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataStatistics.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mActionBar != null) {
                mActionBar.onDestory();
            }
            if (mPresenter != null) {
                mPresenter.unsubscribe();
            }
            ButterKnife.unbind(this);
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "onDestroy-error: " + e.getMessage());
        }
        ZYLog.e(getClass().getSimpleName(), "onDestroy");
    }

    /**
     * 是否设置状态栏颜色
     */
    protected boolean tintStatusBar() {
        return true;
    }

    /**
     * 设置状态栏字体颜色
     *
     * @param isDarkMode true 深色，false 浅色
     */
    public void setDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            ZYStatusBarUtils.setStatusBarDarkMode(this);
        } else {
            ZYStatusBarUtils.setStatusBarLightMode(this);
        }
    }

    protected int getStatusColor() {
        return R.color.c1;
    }

    public void hideActionBar() {
        mActionBar.setVisibility(View.GONE);
    }

    public void showTitle(String title) {
        mActionBar.showTitle(title);
    }

    public void hideActionLeftImg() {
        mActionBar.hideActionLeftImg();
    }


    public void showActionLeftTitle(String title, View.OnClickListener clickListener) {
        mActionBar.showActionLeftTitle(title, clickListener);
    }

    public void showActionRightTitle(String title, View.OnClickListener clickListener) {
        mActionBar.showActionRightTitle(title, clickListener);
    }

    public void showActionRightImg(int res, View.OnClickListener clickListener) {
        mActionBar.showActionRightImg(res, clickListener);
    }

    protected void showWaitDialog(String message) {
        if (mWaitDialog == null) {
            mWaitDialog = new ZYWaitDialog(this);
        }
        mWaitDialog.showWidthMessage(message);
    }

    protected void refreshWaitDialog(String message) {
        mWaitDialog.refreshMessage(message);
    }

    protected void hideWaitDialog() {
        mWaitDialog.dismiss();
    }

    public void setPresenter(P mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void showProgress() {
        showWaitDialog(null);
    }

    @Override
    public void hideProgress() {
        hideWaitDialog();
    }

    @Override
    public void showToast() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }
}
