package com.qudiandu.smartreader.ui.myAudio.view;

import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartreader.ui.myAudio.contract.SRCataloguesContract;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.ui.myAudio.view.viewHolder.SRCataloguesVH;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCataloguesFragment extends ZYListDateFragment<SRCataloguesContract.IPresenter, SRCatalogueNew> implements SRCataloguesContract.IView {

    @Override
    protected void onItemClick(View view, int position) {
        SRCatalogueNew catalogueNew = mAdapter.getItem(position);
        if (catalogueNew != null) {
            mActivity.startActivity(SRCatalogueDetailActivity.createIntent(mActivity, catalogueNew.getId() + ""));
        }
    }

    @Override
    protected ZYBaseViewHolder<SRCatalogueNew> createViewHolder() {
        return new SRCataloguesVH();
    }
}
