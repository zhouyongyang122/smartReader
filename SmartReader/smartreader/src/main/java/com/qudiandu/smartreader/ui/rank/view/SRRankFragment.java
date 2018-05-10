package com.qudiandu.smartreader.ui.rank.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartreader.ui.profile.activity.SRPersonHomeActivity;
import com.qudiandu.smartreader.ui.rank.contract.SRRankContract;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;
import com.qudiandu.smartreader.ui.rank.view.viewHolder.SRRankItemVH;
import com.qudiandu.smartreader.utils.ZYResourceUtils;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRankFragment extends ZYListDateFragment<SRRankContract.IPresenter, SRRank> implements SRRankContract.IView {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setBackgroundColor(ZYResourceUtils.getColor(R.color.white));
        mRefreshRecyclerView.getLoadingView().getView().setBackgroundColor(ZYResourceUtils.getColor(R.color.white));
        mRefreshRecyclerView.getLoadMoreView().setNoMoreText("");
    }

    @Override
    protected void onItemClick(View view, int position) {
        SRRank rank = mAdapter.getItem(position);
        if (rank != null) {
            mActivity.startActivity(SRCatalogueDetailActivity.createIntent(mActivity, rank.show_id + ""));
        }
    }

    @Override
    protected ZYBaseViewHolder<SRRank> createViewHolder() {
        return new SRRankItemVH();
    }

    @Override
    public void showEmpty() {
        showList(false);
    }
}
