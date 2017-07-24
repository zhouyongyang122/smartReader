package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassListVH extends ZYBaseViewHolder<List<SRClass>> {

    List<SRClass> mData;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    ZYBaseRecyclerAdapter<SRClass> adapter;

    ClassListListener listListener;

    public SRClassListVH(ClassListListener listListener) {
        this.listListener = listListener;
    }

    @Override
    public void updateView(List<SRClass> data, int position) {
        if (data != null) {
            mData = data;
            adapter = new ZYBaseRecyclerAdapter<SRClass>(mData) {
                @Override
                public ZYBaseViewHolder<SRClass> createViewHolder(int type) {
                    return new SRClassListItemVH();
                }
            };
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            adapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    listListener.onClassSelecte(adapter.getItem(position),position);
                }
            });
            show();
        }
    }

    @Override
    public void attachTo(ViewGroup viewGroup) {
        super.attachTo(viewGroup);
        hide();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_list;
    }

    public interface ClassListListener {
        void onClassSelecte(SRClass value,int position);
    }
}
