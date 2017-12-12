package com.qudiandu.smartreader.ui.wordStudy.view;

import android.view.View;

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
    protected ZYBaseRecyclerAdapter<SRWordStudyUnit> createAdapter() {

        ZYBaseRecyclerAdapter<SRWordStudyUnit> adapter = super.createAdapter();
        unitsHeaderVH = new SRWordStudyUnitsHeaderVH();
        adapter.addHeader(unitsHeaderVH);
        return adapter;
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
