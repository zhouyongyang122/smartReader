package com.qudiandu.smartreader.ui.task.view;

import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartreader.ui.task.activity.SRTaskCommentActivity;
import com.qudiandu.smartreader.ui.task.contract.SRTaskDetailContract;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskDetailHeaderVH;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskDetailItemVH;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskDetailFragment extends ZYListDateFragment<SRTaskDetailContract.IPresenter, SRTaskFinish> implements SRTaskDetailContract.IView, SRTaskDetailItemVH.TaskDetailItemListener {

    SRTaskDetailHeaderVH headerVH;

    @Override
    protected void onItemClick(View view, int position) {
        SRTaskFinish finish = mAdapter.getItem(position);
        mActivity.startActivity(SRCatalogueDetailActivity.createIntent(mActivity, finish.show_id + ""));
    }

    @Override
    protected void init() {
        super.init();
        headerVH = new SRTaskDetailHeaderVH();
        mAdapter.addHeader(headerVH);
    }

    public void refreshHeader(SRTask task) {
        headerVH.updateView(task, 0);
    }

    @Override
    protected ZYBaseViewHolder<SRTaskFinish> createViewHolder() {
        return new SRTaskDetailItemVH(this);
    }

    @Override
    public void onCommentClick(SRTaskFinish finish) {
        mActivity.startActivity(SRTaskCommentActivity.createIntent(mActivity, finish.show_id + ""));
    }
}
