package com.qudiandu.smartreader.ui.book.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.event.SREventSelectedBook;
import com.qudiandu.smartreader.base.event.SREventSelectedStudyBook;
import com.qudiandu.smartreader.base.mvp.ZYBaseRecyclerFragment;
import com.qudiandu.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadManager;
import com.qudiandu.smartreader.service.downNet.down.ZYIDownBase;
import com.qudiandu.smartreader.ui.SRAppConstants;
import com.qudiandu.smartreader.ui.book.contract.SRBooksContract;
import com.qudiandu.smartreader.ui.book.view.viewHolder.SRBooksAddItemVH;
import com.qudiandu.smartreader.ui.book.view.viewHolder.SRBooksDefItemVH;
import com.qudiandu.smartreader.ui.book.view.viewHolder.SRBooksItemVH;
import com.qudiandu.smartreader.ui.main.activity.SRBookHomeActivity;
import com.qudiandu.smartreader.ui.main.activity.SRGradeActivity;
import com.qudiandu.smartreader.ui.main.model.SRBookFileManager;
import com.qudiandu.smartreader.ui.main.model.SRBookSelectManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.utils.ZYFileUtils;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/8/31.
 */

public class SRBooksFragment extends ZYBaseRecyclerFragment<SRBooksContract.IPresenter> implements SRBooksContract.IView,
        SRBooksItemVH.HomeBookItemListener {

    ZYBaseRecyclerAdapter<SRBook> mAdapter;

    List<ZYBaseViewHolder> mEvents = new ArrayList<ZYBaseViewHolder>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        createAdapter();
        mRefreshRecyclerView.setAdapter(mAdapter);
        mRefreshRecyclerView.setRefreshEnable(false);
        mRefreshRecyclerView.setLoadMoreEnable(false);
        mRefreshRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        int space = ZYScreenUtils.dp2px(mActivity, 15);
        ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setPadding(space, space, space, space);
        mRefreshRecyclerView.getLoadMoreView().setNoMoreText("  ");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadDatas();
    }

    void createAdapter() {
        mAdapter = new ZYBaseRecyclerAdapter<SRBook>(mPresenter.getDatas()) {

            private static final int TYPE_ADD_BOOK = 0;

            private static final int TYPE_DEF_BOOK = 1;

            private static final int TYPE_NORMAL_BOOK = 2;

            @Override
            public int getItemViewType(int position) {

                if (super.getItemViewType(position) == ZYBaseRecyclerAdapter.TYPE_NORMAL) {
                    SRBook book = getItem(position);
                    if (book != null) {
                        if (book.getBook_id_int() < 0) {
                            return TYPE_ADD_BOOK;
                        } else if (book.getBook_id_int() == 0) {
                            return TYPE_DEF_BOOK;
                        } else {
                            return TYPE_NORMAL_BOOK;
                        }
                    }
                }
                return super.getItemViewType(position);
            }

            @Override
            public ZYBaseViewHolder<SRBook> createViewHolder(int type) {
                if (type == TYPE_ADD_BOOK) {
                    return new SRBooksAddItemVH(SRBooksFragment.this);
                } else if (type == TYPE_DEF_BOOK) {
                    return new SRBooksDefItemVH(SRBooksFragment.this);
                } else {
                    return new SRBooksItemVH(SRBooksFragment.this);
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            for (ZYBaseViewHolder viewHolder : mEvents) {
                EventBus.getDefault().unregister(mEvents);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void addEvents(ZYBaseViewHolder viewHolder) {
        mEvents.add(viewHolder);
    }

    @Override
    public void onHomeBookItemClick(SRBook book, int position) {
        if (book.getBook_id_int() < 0) {
            startActivity(SRGradeActivity.createIntent(mActivity));
        } else {
            EventBus.getDefault().post(new SREventSelectedStudyBook(mPresenter.getClassId(), book.getBook_id_int()));
        }
    }

    @Override
    public void onHomeBookItemDel(final SRBook book, int position) {
        new AlertDialog.Builder(mActivity).setTitle("课程删除")
                .setMessage("是否删除 " + book.getName() + "?")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.getDatas().remove(book);
                        ZYFileUtils.delete(book.savePath);
                        mAdapter.notifyDataSetChanged();
                        ZYDownloadManager.getInstance().delBook(book);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBookSelected(SREventSelectedBook eventSelectedBook) {
        ZYLog.e(getClass().getSimpleName(), "eventBookSelected: " + SRBookSelectManager.getInstance().getAddBooksSize());
        List<ZYIDownBase> books = new ArrayList<ZYIDownBase>();
        for (SRBook book : SRBookSelectManager.getInstance().getAddBooks()) {
            book.savePath = SRBookFileManager.getBookPath(book.book_id);
            books.add(book);
        }
        ZYDownloadManager.getInstance().addBooks(books);
        mPresenter.getDatas().addAll(SRBookSelectManager.getInstance().getAddBooks());
        mAdapter.notifyDataSetChanged();
        SRBookSelectManager.getInstance().clearAddBooks();
    }
}
