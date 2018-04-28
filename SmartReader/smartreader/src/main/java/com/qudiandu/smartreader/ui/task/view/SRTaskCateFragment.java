package com.qudiandu.smartreader.ui.task.view;

import android.view.View;

import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.model.bean.SRTaskTitle;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskCate;
import com.qudiandu.smartreader.ui.task.contract.SRTaskCateContract;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskCateItemTitleVH;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskCateItemVH;

/**
 * Created by ZY on 17/7/23.
 */

public class SRTaskCateFragment extends ZYListDateFragment<SRTaskCateContract.IPresenter, Object> implements SRTaskCateContract.IView {

    final int TITLE_TYPE = 0;

    final int CATE_TYPE = 1;

    @Override
    protected void init() {
        super.init();
        mRefreshRecyclerView.setRefreshEnable(false);
    }

    @Override
    protected void onItemClick(View view, int position) {
        if (mAdapter.getItem(position) instanceof SRTaskCate) {
            int index = 0;
            for (Object object : mPresenter.getDataList()) {
                if (object instanceof SRTaskCate) {
                    SRTaskCate taskCate = (SRTaskCate) object;
                    if (index == position) {
                        taskCate.isCheck = true;
                    } else {
//                        taskCate.isCheck = false;
                    }
                }
                index++;
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected ZYBaseRecyclerAdapter<Object> createAdapter() {
        final ZYBaseRecyclerAdapter<Object> adapter = new ZYBaseRecyclerAdapter<Object>(mPresenter.getDataList()) {
            @Override
            public ZYBaseViewHolder<Object> createViewHolder(int type) {
                switch (type) {
                    case TITLE_TYPE:
                        return new SRTaskCateItemTitleVH();
                    case CATE_TYPE:
                        return new SRTaskCateItemVH();
                }
                return new SRTaskCateItemVH();
            }

            @Override
            public int getItemViewType(int position) {
                if (mAdapter.getItem(position) instanceof SRTaskTitle) {
                    return TITLE_TYPE;
                } else if (mAdapter.getItem(position) instanceof SRTaskCate) {
                    return CATE_TYPE;
                }
                return super.getItemViewType(position);
            }
        };
        return adapter;
    }

    @Override
    protected ZYBaseViewHolder<Object> createViewHolder() {
        return null;
    }
}
