package com.qudiandu.smartreader.ui.task.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartreader.ui.task.activity.SRTaskCommentActivity;
import com.qudiandu.smartreader.ui.task.activity.SRTaskListenActivity;
import com.qudiandu.smartreader.ui.task.activity.SRTaskProblemActivity;
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
        if (finish != null) {
            if (mPresenter.isProblemTask()) {
                if (mPresenter.getTask().ctype == SRTask.TASK_TYPE_LISTEN) {
                    startActivity(SRTaskListenActivity.createIntent(mActivity, finish));
                } else {
                    startActivity(SRTaskProblemActivity.createIntent(mActivity, finish));
                }
            } else {
                startActivity(SRCatalogueDetailActivity.createIntent(mActivity, finish.show_id + ""));
            }
        }
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
        return new SRTaskDetailItemVH(this, mPresenter.getTask().ctype != SRTask.TASK_TYPE_LISTEN);
    }

    @Override
    public void onCommentClick(SRTaskFinish finish, int position) {
        if (!TextUtils.isEmpty(finish.comment)) {
            return;
        }
        finish.ctype = mPresenter.getTask().ctype;
        startActivityForResult(SRTaskCommentActivity.createIntent(mActivity, finish), position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            SRTaskFinish finish = mAdapter.getItem(requestCode);
            finish.comment = data.getStringExtra("comment");
            mAdapter.notifyDataSetChanged();
        }
    }
}
