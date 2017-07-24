package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskOKVH extends ZYBaseViewHolder {

    TaskOKListener listener;

    @Bind(R.id.textName)
    TextView textName;

    public SRTaskOKVH(TaskOKListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOKClick();
            }
        });
    }

    public void setText(String text) {
        if (textName != null) {
            textName.setText(text);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_ok;
    }

    public interface TaskOKListener {
        void onOKClick();
    }
}
