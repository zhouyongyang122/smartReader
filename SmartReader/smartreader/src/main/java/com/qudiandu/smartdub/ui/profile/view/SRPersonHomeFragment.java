package com.qudiandu.smartdub.ui.profile.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartdub.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartdub.base.mvp.ZYListDateFragment;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartdub.ui.myAudio.view.viewHolder.SRCataloguesVH;
import com.qudiandu.smartdub.ui.profile.contact.SRPersonHomeContract;
import com.qudiandu.smartdub.ui.profile.view.viewHolder.SRPersonHomeUserVH;

/**
 * Created by ZY on 18/3/7.
 */

public class SRPersonHomeFragment extends ZYListDateFragment<SRPersonHomeContract.IPresenter, SRCatalogueNew> implements SRPersonHomeContract.IView {

    SRPersonHomeUserVH homeUserVH;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshRecyclerView.setRefreshEnable(false);
    }

    @Override
    protected void onItemClick(View view, int position) {
        SRCatalogueNew catalogueNew = mAdapter.getItem(position);
        if (catalogueNew != null) {
            mActivity.startActivity(SRCatalogueDetailActivity.createIntent(mActivity, catalogueNew.getId() + ""));
        }
    }

    @Override
    protected ZYBaseRecyclerAdapter<SRCatalogueNew> createAdapter() {
        ZYBaseRecyclerAdapter<SRCatalogueNew> adapter = super.createAdapter();
        homeUserVH = new SRPersonHomeUserVH();
        adapter.addHeader(homeUserVH);
        return adapter;
    }

    @Override
    protected ZYBaseViewHolder<SRCatalogueNew> createViewHolder() {
        return new SRCataloguesVH();
    }

    @Override
    public void showList(boolean isHasMore) {

        homeUserVH.updateView(mPresenter.getUser(), 0);

        super.showList(isHasMore);
    }
}
