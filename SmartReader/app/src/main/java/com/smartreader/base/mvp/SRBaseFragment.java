package com.smartreader.base.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.smartreader.base.view.SRWaitDialog;
import com.smartreader.base.utils.SRLog;

import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/14.
 */

public class SRBaseFragment<P extends SRIBasePresenter> extends Fragment implements SRIBaseView<P> {

    protected P mPresenter;

    protected Activity mActivity;

    protected SRWaitDialog mWaitDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            if (mPresenter != null) {
                mPresenter.subscribe();
            }
        } catch (Exception e) {
            SRLog.e(getClass().getSimpleName(), "onViewCreated-error:" + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SRLog.e(getClass().getSimpleName(), "onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (mPresenter != null) {
                mPresenter.unsubscribe();
            }
        } catch (Exception e) {
            SRLog.e(getClass().getSimpleName(), "onDestroyView-error:" + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SRLog.e(getClass().getSimpleName(), "onDestroy");
        try {
            ButterKnife.unbind(this);
        } catch (Exception e) {
            SRLog.e(getClass().getSimpleName(), "onDestroy-error:" + e.getMessage());
        }
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    protected void showWaitDialog(String message) {
        if (mWaitDialog == null) {
            mWaitDialog = new SRWaitDialog(mActivity);
        }
        mWaitDialog.showWidthMessage(message);
    }

    protected void refreshWaitDialog(String message) {
        mWaitDialog.refreshMessage(message);
    }

    protected void hideWaitDialog() {
        mWaitDialog.dismiss();
    }

    @Override
    public void viewAction(Object message, String action) {
        if (SRIBaseView.IBASE_VIEW_ACTION_FINISH.equals(action) && mActivity != null) {
            mActivity.finish();
        }
    }
}
