package com.smartreader.ui.main.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.ui.main.model.bean.SRBook;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

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
                        return new SRHomeBookItemVH();
                    }
                };

                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                recyclerView.setAdapter(mAdapter);
                recyclerView.setNestedScrollingEnabled(false);
                mAdapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        bookListener.onItemClick(books.get(position), position);
                    }
                });
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_home_book;
    }

    public interface HomeBookListener {
        void onItemClick(SRBook book, int position);
    }
}
