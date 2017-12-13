package com.qudiandu.smartreader.ui.wordStudy.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.wordStudy.activity.SRWordStudyHomeActivity;
import com.qudiandu.smartreader.ui.wordStudy.contract.SRWordStudyUnitsContract;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyUnit;
import com.qudiandu.smartreader.ui.wordStudy.view.viewHolder.SRWordStudyUnitsHeaderVH;
import com.qudiandu.smartreader.ui.wordStudy.view.viewHolder.SRWordStudyUnitsItemVH;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyUnitsFragment extends ZYListDateFragment<SRWordStudyUnitsContract.IPresenter, SRWordStudyUnit> implements SRWordStudyUnitsContract.IView {

    SRWordStudyUnitsHeaderVH unitsHeaderVH;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshRecyclerView.setRefreshEnable(false);
        mRefreshRecyclerView.setLoadMoreEnable(false);
    }

    @Override
    protected ZYBaseRecyclerAdapter<SRWordStudyUnit> createAdapter() {

        ZYBaseRecyclerAdapter<SRWordStudyUnit> adapter = super.createAdapter();
        unitsHeaderVH = new SRWordStudyUnitsHeaderVH();
        adapter.addHeader(unitsHeaderVH);
        return adapter;
    }

    @Override
    public void showList(boolean isHasMore) {
        super.showList(false);
    }

    @Override
    protected void onItemClick(View view, int position) {
        SRWordStudyUnit unit = mAdapter.getItem(position);
        if (unit != null) {
            startActivity(SRWordStudyHomeActivity.createIntent(mActivity, unit));
        }
    }

    @Override
    protected ZYBaseViewHolder<SRWordStudyUnit> createViewHolder() {
        return new SRWordStudyUnitsItemVH();
    }
}
