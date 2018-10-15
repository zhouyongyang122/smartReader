package com.qudiandu.smartreader.ui.invite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 下午3:57$
 */
public class SRInviteInfoFragment extends ZYListDateFragment<SRInviteInfoContract.IPresenter, SRInviteInfo> implements SRInviteInfoContract.IView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mRootView.setBackgroundColor(ZYResourceUtils.getColor(R.color.c1));
        ZYSwipeRefreshRecyclerView swipeRefreshRecyclerView = (ZYSwipeRefreshRecyclerView) mRefreshRecyclerView;
        swipeRefreshRecyclerView.setBackgroundResource(R.drawable.bg_corner4dp_white);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) swipeRefreshRecyclerView.getLayoutParams();
        layoutParams.topMargin = ZYScreenUtils.dp2px(mActivity, 30);
        layoutParams.bottomMargin = ZYScreenUtils.dp2px(mActivity, 30);
        layoutParams.leftMargin = ZYScreenUtils.dp2px(mActivity, 15);
        layoutParams.rightMargin = ZYScreenUtils.dp2px(mActivity, 15);

        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected ZYBaseViewHolder<SRInviteInfo> createViewHolder() {
        return new SRInviteInfoVH();
    }
}
