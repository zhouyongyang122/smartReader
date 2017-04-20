package com.smartreader.ui.main.view;

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

import com.smartreader.R;
import com.smartreader.base.event.SREventSelectedBook;
import com.smartreader.base.mvp.ZYListDateFragment;
import com.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.ui.login.model.SRUserManager;
import com.smartreader.ui.main.contract.SRBookListContract;
import com.smartreader.ui.main.model.SRAddBookManager;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.main.view.viewhodler.SRBookListItemVH;
import com.smartreader.utils.ZYLog;
import com.smartreader.utils.ZYResourceUtils;
import com.smartreader.utils.ZYScreenUtils;
import com.smartreader.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookListFragment extends ZYListDateFragment<SRBookListContract.IPresenter, SRBook> implements SRBookListContract.IView, SRBookListItemVH.BookListItemListener {

    TextView textAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) super.onCreateView(inflater, container, savedInstanceState);

        ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setPadding(ZYScreenUtils.dp2px(mActivity, 15), 0, ZYScreenUtils.dp2px(mActivity, 15), ZYScreenUtils.dp2px(mActivity, 50));

        mRefreshRecyclerView.setLoadMoreEnable(false);

        textAdd = new TextView(mActivity);
        textAdd.setGravity(Gravity.CENTER);
        textAdd.setTextColor(ZYResourceUtils.getColor(R.color.white));
        textAdd.setBackgroundColor(ZYResourceUtils.getColor(R.color.c7));
        textAdd.setTextSize(18);
        textAdd.setText("确认添加");
        textAdd.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZYScreenUtils.dp2px(mActivity, 50));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        view.addView(textAdd, layoutParams);

        textAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SRAddBookManager.getInstance().getAddBooksSize() <= 0) {
                    ZYToast.show(mActivity, "还没有选择书籍!");
                } else {
                    EventBus.getDefault().post(new SREventSelectedBook());
                    mPresenter.reportAddBookts();
                    finish();
                }
            }
        });
        return view;
    }

    @Override
    protected ZYBaseViewHolder<SRBook> createViewHolder() {
        return new SRBookListItemVH(this);
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mActivity, 3);
    }

    @Override
    public void showList(boolean isHasMore) {
        super.showList(false);
        textAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBookItemSelect(SRBook book, boolean isSelected) {

        if (SRUserManager.getInstance().isGuesterUser(true)) {
            return;
        }

        if (isSelected) {
            SRAddBookManager.getInstance().addBook(book);
        } else {
            SRAddBookManager.getInstance().removeBook(book);
        }

        if (SRAddBookManager.getInstance().getAddBooksSize() > 0) {
            textAdd.setText("确认添加 (" + SRAddBookManager.getInstance().getAddBooksSize() + ")");
        } else {
            textAdd.setText("确认添加");
        }
        ZYLog.e(getClass().getSimpleName(), "onBookItemSelect: " + SRAddBookManager.getInstance().getAddBooksSize());
    }
}
