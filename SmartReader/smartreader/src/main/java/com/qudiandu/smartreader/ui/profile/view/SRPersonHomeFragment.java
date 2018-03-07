package com.qudiandu.smartreader.ui.profile.view;

import android.view.View;

import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.ui.myAudio.view.viewHolder.SRCataloguesVH;
import com.qudiandu.smartreader.ui.profile.contact.SRPersonHomeContract;
import com.qudiandu.smartreader.ui.profile.view.viewHolder.SRPersonHomeUserVH;

/**
 * Created by ZY on 18/3/7.
 */

public class SRPersonHomeFragment extends ZYListDateFragment<SRPersonHomeContract.IPresenter, SRCatalogueNew> implements SRPersonHomeContract.IView {

    SRPersonHomeUserVH homeUserVH;

    @Override
    protected void onItemClick(View view, int position) {
        SRCatalogueNew catalogueNew = mAdapter.getItem(position);
        if (catalogueNew != null) {

        }
    }

    @Override
    protected ZYBaseRecyclerAdapter<SRCatalogueNew> createAdapter() {
        ZYBaseRecyclerAdapter<SRCatalogueNew> adapter = createAdapter();
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
