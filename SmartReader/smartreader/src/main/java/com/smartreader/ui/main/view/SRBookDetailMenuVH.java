package com.smartreader.ui.main.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.ui.main.model.bean.SRCatalogue;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookDetailMenuVH extends ZYBaseViewHolder<List<SRCatalogue>> {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<SRCatalogue> mData;

    ZYBaseRecyclerAdapter<SRCatalogue> adapter;

    BookDetailMenuListener listener;

    private int defSelPosition;

    public SRBookDetailMenuVH(BookDetailMenuListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(final List<SRCatalogue> data, int position) {
        if (data != null) {
            mData = data;
        }
        adapter = new ZYBaseRecyclerAdapter<SRCatalogue>(mData) {
            @Override
            public ZYBaseViewHolder<SRCatalogue> createViewHolder(int type) {
                return new SRBookDetailMenuItemVH(defSelPosition);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SRCatalogue catalogue = mData.get(position);
                if (catalogue.getCatalogue_id() > 0) {
                    listener.onItemClick(catalogue, position);
                    close();
                }
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_book_detail_menu;
    }

    @OnClick({R.id.textClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textClose:
                close();
                break;
        }
    }

    public void close() {
        mItemView.setVisibility(View.GONE);
        ViewGroup parent = (ViewGroup) mItemView.getParent();
        parent.removeView(mItemView);
        listener.onMenuClose();
    }

    public void setDefSelPosition(int defSelPosition) {
        this.defSelPosition = defSelPosition;
    }

    public interface BookDetailMenuListener {
        void onMenuClose();

        void onItemClick(SRCatalogue catalogue, int position);
    }
}
