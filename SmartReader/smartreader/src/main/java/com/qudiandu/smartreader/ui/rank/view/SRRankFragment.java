package com.qudiandu.smartreader.ui.rank.view;

import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.rank.contract.SRRankContract;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;
import com.qudiandu.smartreader.ui.rank.view.viewHolder.SRRankItemVH;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRankFragment extends ZYListDateFragment<SRRankContract.IPresenter, SRRank> implements SRRankContract.IView {

    @Override
    protected void onItemClick(View view, int position) {
        SRRank rank = mAdapter.getItem(position);
        if(rank != null){

        }
    }

    @Override
    protected ZYBaseViewHolder<SRRank> createViewHolder() {
        return new SRRankItemVH();
    }
}
