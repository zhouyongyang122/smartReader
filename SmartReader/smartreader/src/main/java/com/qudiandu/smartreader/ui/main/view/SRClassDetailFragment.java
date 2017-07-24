package com.qudiandu.smartreader.ui.main.view;

import android.view.View;

import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRClassDetailContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassDetailHeaderVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassDetailItemVH;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailFragment extends ZYListDateFragment<SRClassDetailContract.IPresenter, SRUser> implements SRClassDetailContract.IView {

    SRClassDetailHeaderVH headerVH;

    @Override
    protected void init() {
        super.init();
        mRefreshRecyclerView.getRecyclerView().setPadding(ZYScreenUtils.dp2px(mActivity, 5), 0, ZYScreenUtils.dp2px(mActivity, 5), 0);
        mRefreshRecyclerView.setRefreshEnable(false);
        headerVH = new SRClassDetailHeaderVH();
        mAdapter.addHeader(headerVH);
    }

    public void refreshHeader(SRClass data) {
        headerVH.updateView(data, 0);
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected ZYBaseViewHolder<SRUser> createViewHolder() {
        return new SRClassDetailItemVH();
    }
}
