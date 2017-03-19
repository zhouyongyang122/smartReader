package com.smartreader.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smartreader.R;
import com.smartreader.base.view.SRIListView;
import com.smartreader.base.view.SRISwipeRecyclerView;
import com.smartreader.base.view.SRSwipeRefreshRecyclerView;

/**
 * Created by ZY on 17/3/16.
 */

public class SRBaseRecyclerFragment extends SRBaseFragment implements SRIListView {

    protected SRISwipeRecyclerView mRefreshRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = createRootView(inflater, container);

        initView((ViewGroup) rootView);

        return rootView;
    }

    protected View createRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sr_fragment_base_list, container, false);
    }

    protected void initView(ViewGroup rootView) {
        mRefreshRecyclerView = new SRSwipeRefreshRecyclerView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rootView.addView((View) mRefreshRecyclerView, params);
        mRefreshRecyclerView.getLoadingView().setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetry();
            }
        });
    }

    protected void onRetry() {
        showLoading();
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void showList(boolean isHasMore) {
        mRefreshRecyclerView.showList(isHasMore);
    }

    @Override
    public void showEmpty() {
        mRefreshRecyclerView.showEmpty();
    }

    @Override
    public void showError() {
        mRefreshRecyclerView.showError();
    }

    @Override
    public void showLoading() {
        mRefreshRecyclerView.showLoading();
    }
}
