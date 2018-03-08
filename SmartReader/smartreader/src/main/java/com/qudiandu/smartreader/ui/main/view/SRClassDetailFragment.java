package com.qudiandu.smartreader.ui.main.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRClassDetailContract;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassDetailItemVH;
import com.qudiandu.smartreader.ui.profile.activity.SRPersonHomeActivity;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailFragment extends ZYListDateFragment<SRClassDetailContract.IPresenter, SRUser> implements SRClassDetailContract.IView {

    @Override
    protected void init() {
        super.init();
        mRefreshRecyclerView.setRefreshEnable(false);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mActivity, 5);
    }

    @Override
    protected void onItemClick(View view, int position) {
        SRUser user = mAdapter.getItem(position);
        if (user != null) {
            startActivity(SRPersonHomeActivity.createIntent(mActivity, user));
        }
    }

    @Override
    protected ZYBaseViewHolder<SRUser> createViewHolder() {
        return new SRClassDetailItemVH();
    }
}
