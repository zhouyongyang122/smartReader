package com.smartreader.base.view;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.smartreader.base.adapter.SRBaseRecyclerAdapter;
import com.smartreader.base.viewHolder.SRLoadMoreVH;

/**
 * Created by ZY on 17/3/16.
 */

public interface SRISwipeRecyclerView extends SRIListView {

    void setRefreshListener(SRRefreshListener refreshListener);

    void setLoadMoreEnable(boolean loadMoreEnable);

    void setRefreshEnable(boolean refreshEnable);

    void setRefreshing(boolean isRefreshing);

    SRILoadingView getLoadingView();

    boolean isRefresh();

    void setAdapter(@NonNull SRBaseRecyclerAdapter adapter);

    void setMoreViewHolder(@NonNull SRLoadMoreVH moreViewHolder);

    void setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager);

    void setLoadingView(@NonNull SRILoadingView loadingView);

    SwipeRefreshLayout getSwipeRefreshLayout();

    RecyclerView getRecyclerView();
}
