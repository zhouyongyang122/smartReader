package com.qudiandu.smartreader.ui.wordStudy.view.viewHolder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyWord;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyHomeMenuVH extends ZYBaseViewHolder<List<SRWordStudyWord>> {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public void updateView(List<SRWordStudyWord> data, int position) {
        if (data != null) {
            ZYBaseRecyclerAdapter<SRWordStudyWord> adapter = new ZYBaseRecyclerAdapter<SRWordStudyWord>(data) {
                @Override
                public ZYBaseViewHolder<SRWordStudyWord> createViewHolder(int type) {
                    return new SRWordStudyHomeMenuItemVH();
                }
            };
            adapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    hide();
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }
    }

    @OnClick({R.id.imgClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                hide();
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_word_study_home_menu;
    }
}
