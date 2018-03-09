package com.qudiandu.smartreader.ui.main.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRClassDetailContract;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassDetailHeaderVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassDetailItemVH;
import com.qudiandu.smartreader.ui.profile.activity.SRPersonHomeActivity;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailFragment extends ZYListDateFragment<SRClassDetailContract.IPresenter, SRUser> implements SRClassDetailContract.IView, SRClassDetailItemVH.ClassDetailItemListener {

    SRClassDetailHeaderVH headerVH;

    boolean mEdit;

    @Override
    protected void init() {
        super.init();
        mRefreshRecyclerView.setRefreshEnable(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshRecyclerView.getRecyclerView().setPadding(ZYScreenUtils.dp2px(mActivity, 10), 0, ZYScreenUtils.dp2px(mActivity, 10), 0);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mActivity, 4);
    }

    @Override
    protected ZYBaseRecyclerAdapter<SRUser> createAdapter() {
        ZYBaseRecyclerAdapter<SRUser> adapter = super.createAdapter();
        headerVH = new SRClassDetailHeaderVH(true);
        adapter.addHeader(headerVH);
        headerVH.updateView(mPresenter.getMClass(), 0);
        return adapter;
    }

    @Override
    protected void onItemClick(View view, int position) {
        SRUser user = mAdapter.getItem(position);
        if (!mEdit && user != null) {
            startActivity(SRPersonHomeActivity.createIntent(mActivity, user));
        }
    }

    @Override
    public void onDelClick(final SRUser user) {
        new AlertDialog.Builder(mActivity).setTitle("删除").setMessage("是否删除学生" + user.nickname + "?")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.delUser(user);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void delUserSuc() {
        mAdapter.notifyDataSetChanged();
    }

    public void edit(boolean edit) {
        mPresenter.edit(edit);
        mEdit = edit;
        mAdapter.notifyDataSetChanged();
        headerVH.editClassName(edit);
        String name = headerVH.updateClassName();
        if (!edit && name != null) {
            mPresenter.updateClassName(name);
        }
    }

    @Override
    protected ZYBaseViewHolder<SRUser> createViewHolder() {
        return new SRClassDetailItemVH(this);
    }
}
