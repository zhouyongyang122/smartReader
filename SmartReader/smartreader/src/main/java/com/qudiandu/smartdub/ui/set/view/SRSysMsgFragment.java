package com.qudiandu.smartdub.ui.set.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartdub.base.mvp.ZYListDateFragment;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.ui.set.activity.SRFeedBackActivity;
import com.qudiandu.smartdub.ui.set.activity.SRMsgDetailActivity;
import com.qudiandu.smartdub.ui.set.contract.SRSysMsgContract;
import com.qudiandu.smartdub.ui.set.model.bean.SRSysMsg;
import com.qudiandu.smartdub.ui.set.view.viewHolder.SRSysMsgVH;
import com.qudiandu.smartdub.ui.web.SRWebViewActivity;

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
        SRSysMsg msg = mAdapter.getItem(position);
        if (msg != null && !TextUtils.isEmpty(msg.url)) {
            startActivity(SRWebViewActivity.createIntent(mActivity, msg.url, msg.title));
            return;
        }

        if (msg != null) {
            if (!TextUtils.isEmpty(msg.pic)) {
                startActivity(SRMsgDetailActivity.getIntent(mActivity, msg));
                return;
            }
            startActivity(new Intent(mActivity, SRFeedBackActivity.class));
        }
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
