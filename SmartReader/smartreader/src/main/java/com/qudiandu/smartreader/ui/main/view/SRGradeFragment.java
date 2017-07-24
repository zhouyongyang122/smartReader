package com.qudiandu.smartreader.ui.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.activity.SRBookListActivity;
import com.qudiandu.smartreader.ui.main.contract.SRGradeContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRGrade;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRGradeItemVH;
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

/**
 * Created by ZY on 17/3/31.
 */

public class SRGradeFragment extends ZYListDateFragment<SRGradeContract.IPresenter, SRGrade> implements SRGradeContract.IView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.setRefreshEnable(false);
        int space = ZYScreenUtils.dp2px(mActivity, 15);
        mRefreshRecyclerView.getRecyclerView().setPadding(space, 0, space, 0);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c2));
        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {
        SRGrade grade = mPresenter.getDataList().get(position);
        mActivity.startActivity(SRBookListActivity.createIntent(mActivity, grade.grade_id + "", mPresenter.isTaskSel()));
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mActivity, 2);
    }

    @Override
    protected ZYBaseViewHolder<SRGrade> createViewHolder() {
        return new SRGradeItemVH();
    }
}
