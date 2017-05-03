package com.smartreader.ui.set.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartreader.base.mvp.ZYListDateFragment;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.ui.set.contract.SRSysMsgContract;
import com.smartreader.ui.set.model.bean.SRSysMsg;
import com.smartreader.ui.set.view.viewHolder.SRSysMsgVH;

/**
 * Created by ZY on 17/4/9.
 */

public class SRSysMsgFragment extends ZYListDateFragment<SRSysMsgContract.IPresenter, SRSysMsg> implements SRSysMsgContract.IView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.getLoadingView().setEmptyText("还没有消息哦");
        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    public void showList(boolean isHasMore) {
        super.showList(false);
    }

    @Override
    protected ZYBaseViewHolder<SRSysMsg> createViewHolder() {
        return new SRSysMsgVH();
    }
}
