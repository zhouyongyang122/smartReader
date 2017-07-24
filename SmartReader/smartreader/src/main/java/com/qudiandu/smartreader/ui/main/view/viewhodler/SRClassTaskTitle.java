package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.model.bean.SRTaskTitle;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/23.
 */

public class SRClassTaskTitle extends ZYBaseViewHolder<Object> {

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Override
    public void updateView(Object data, int position) {
        if (data != null && data instanceof SRTaskTitle) {
            textTitle.setText(((SRTaskTitle)data).title);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_task_title;
    }
}
