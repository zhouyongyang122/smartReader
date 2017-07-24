package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassListItemVH extends ZYBaseViewHolder<SRClass> {

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Override
    public void updateView(SRClass data, int position) {
        if (data != null) {
            textTitle.setText(data.class_name);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_list_item;
    }
}
