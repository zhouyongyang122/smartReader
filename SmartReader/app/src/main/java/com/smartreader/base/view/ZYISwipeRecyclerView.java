package com.smartreader.base.view;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.smartreader.base.viewHolder.ZYLoadMoreVH;

/**
 * Created by ZY on 17/3/16.
 */

public interface ZYISwipeRecyclerView extends ZYIListView {

    void setRefreshListener(ZYRefreshListener refreshListener);

    void setLoadMoreEnable(boolean loadMoreEnable);

    void setRefreshEnable(boolean refreshEnable);

    void setRefreshing(boolean isRefreshing);

    ZYILoadingView getLoadingView();

    boolean isRefresh();

    void setAdapter(@NonNull ZYBaseRecyclerAdapter adapter);

    void setMoreViewHolder(@NonNull ZYLoadMoreVH moreViewHolder);

    void setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager);

    void setLoadingView(@NonNull ZYILoadingView loadingView);

    SwipeRefreshLayout getSwipeRefreshLayout();

    RecyclerView getRecyclerView();
}
