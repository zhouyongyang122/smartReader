package com.smartreader.ui.main.view;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.service.downNet.down.ZYDownloadManager;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.utils.ZYFileUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/27.
 */

public class SRHomeBookVH extends ZYBaseViewHolder<List<SRBook>> {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.textEdit)
    TextView textEdit;

    private HomeBookListener bookListener;

    private boolean isEditing;

    private ZYBaseRecyclerAdapter<SRBook> mAdapter;
    private List<SRBook> books = new ArrayList<>();

    public SRHomeBookVH(HomeBookListener listener) {
        bookListener = listener;
    }

    @Override
    public void updateView(final List<SRBook> data, int position) {
        if (data != null && data.size() > 1) {
            books = data;

            if (mAdapter == null) {
                mAdapter = new ZYBaseRecyclerAdapter<SRBook>(books) {
                    @Override
                    public ZYBaseViewHolder<SRBook> createViewHolder(int type) {
                        return new SRHomeBookItemVH(new SRHomeBookItemVH.HomeBookItemListener() {
                            @Override
                            public void onHomeBookItemClick(SRBook book, int position) {
                                bookListener.onItemClick(book, position);
                            }

                            @Override
                            public void onHomeBookItemDel(final SRBook book, int position) {
                                new AlertDialog.Builder(mContext).setTitle("课程删除")
                                        .setMessage("是否删除 " + book.getName() + "?")
                                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                books.remove(book);
                                                ZYFileUtils.delete(book.savePath);
                                                mAdapter.notifyDataSetChanged();
                                                if(!book.isFinished()) {
                                                    ZYDownloadManager.getInstance().stopDown(book);
                                                }
                                                book.delete();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create().show();
                            }
                        });
                    }
                };

                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                recyclerView.setAdapter(mAdapter);
                recyclerView.setNestedScrollingEnabled(false);
            }
        }
    }

    @OnClick({R.id.textEdit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textEdit:
                changeEidtStatus();
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_home_book;
    }

    public void refreshData() {
        mAdapter.notifyDataSetChanged();
    }

    public void changeEidtStatus() {
        if (isEditing) {
            isEditing = false;
            textEdit.setText("编辑");
            for (SRBook book : books) {
                book.isDeleteStatus = false;
                book.isCanDelete = false;
            }
            refreshData();
        } else {
            isEditing = true;
            textEdit.setText("取消");
            for (SRBook book : books) {
                book.isDeleteStatus = true;
                if (book.getBook_id_int() > 0) {
                    book.isCanDelete = true;
                }
            }
            refreshData();
        }
    }

    public interface HomeBookListener {
        void onItemClick(SRBook book, int position);
    }
}
