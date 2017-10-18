package com.qudiandu.smartreader.ui.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRClassUsersContract;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassUserItemVH;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskOKVH;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassUsersFragment extends ZYListDateFragment<SRClassUsersContract.IPresenter, SRUser> implements SRClassUsersContract.IView, SRTaskOKVH.TaskOKListener {

    SRTaskOKVH okvh;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        okvh = new SRTaskOKVH(this);
        okvh.attachTo((ViewGroup) view);
        okvh.setText("移除班级");
        okvh.updateView(null, 0);
        okvh.hide();
    }

    @Override
    protected void onItemClick(View view, int position) {
        SRUser user = mAdapter.getItem(position);
        if (user.uid.equals(SRUserManager.getInstance().getUser().uid)) {
            return;
        }
        user.isCheck = !user.isCheck;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected ZYBaseViewHolder<SRUser> createViewHolder() {
        return new SRClassUserItemVH();
    }

    @Override
    public void showList(boolean isHasMore) {
        super.showList(isHasMore);
        okvh.show();
    }

    @Override
    public void delUserSuc() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOKClick() {
        mPresenter.removeUsers();
    }
}
