package com.qudiandu.smartdub.ui.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.event.SREventSelectedBook;
import com.qudiandu.smartdub.base.mvp.ZYListDateFragment;
import com.qudiandu.smartdub.base.view.ZYSwipeRefreshRecyclerView;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.main.contract.SRBookSelectContract;
import com.qudiandu.smartdub.ui.main.model.SRBookSelectManager;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;
import com.qudiandu.smartdub.ui.main.view.viewhodler.SRBookSelectItemVH;
import com.qudiandu.smartdub.ui.task.activity.SRTaskCateActivity;
import com.qudiandu.smartdub.utils.ZYLog;
import com.qudiandu.smartdub.utils.ZYResourceUtils;
import com.qudiandu.smartdub.utils.ZYScreenUtils;
import com.qudiandu.smartdub.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookSelectFragment extends ZYListDateFragment<SRBookSelectContract.IPresenter, SRBook> implements SRBookSelectContract.IView, SRBookSelectItemVH.BookListItemListener {

    TextView textAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.setLoadMoreEnable(false);

        if (mPresenter.isTaskSel()) {
            ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setPadding(ZYScreenUtils.dp2px(mActivity, 15), 0, ZYScreenUtils.dp2px(mActivity, 15), 0);
        } else {
            ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setPadding(ZYScreenUtils.dp2px(mActivity, 15), 0, ZYScreenUtils.dp2px(mActivity, 15), ZYScreenUtils.dp2px(mActivity, 50));
            textAdd = new TextView(mActivity);
            textAdd.setGravity(Gravity.CENTER);
            textAdd.setTextColor(ZYResourceUtils.getColor(R.color.white));
            textAdd.setBackgroundColor(ZYResourceUtils.getColor(R.color.c2));
            textAdd.setTextSize(20);
            textAdd.setText("确认添加");
            textAdd.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZYScreenUtils.dp2px(mActivity, 50));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            view.addView(textAdd, layoutParams);

            textAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SRBookSelectManager.getInstance().getAddBooksSize() <= 0) {
                        ZYToast.show(mActivity, "还没有选择书籍!");
                    } else {
                        EventBus.getDefault().post(new SREventSelectedBook());
                        mPresenter.reportAddBookts();
                        finish();
                    }
                }
            });
        }
        return view;
    }

    @Override
    protected ZYBaseViewHolder<SRBook> createViewHolder() {
        return new SRBookSelectItemVH(this, mPresenter.isTaskSel());
    }

    @Override
    protected void onItemClick(View view, int position) {
        SRBook book = mAdapter.getItem(position);
        mActivity.startActivity(SRTaskCateActivity.createIntent(mActivity, book.book_id));
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mActivity, 3);
    }

    @Override
    public void showList(boolean isHasMore) {
        super.showList(false);
        if (textAdd != null) {
            textAdd.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBookItemSelect(SRBook book, boolean isSelected) {

        if (SRUserManager.getInstance().isGuesterUser(true)) {
            return;
        }

        if (isSelected) {
            book.grade = mPresenter.getGradeName();
            SRBookSelectManager.getInstance().addBook(book);
        } else {
            SRBookSelectManager.getInstance().removeBook(book);
        }

        if (SRBookSelectManager.getInstance().getAddBooksSize() > 0) {
            textAdd.setText("确认添加 (" + SRBookSelectManager.getInstance().getAddBooksSize() + ")");
        } else {
            textAdd.setText("确认添加");
        }
        ZYLog.e(getClass().getSimpleName(), "onBookItemSelect: " + SRBookSelectManager.getInstance().getAddBooksSize());
    }
}
