package com.qudiandu.smartdub.ui.task.view.viewHolder;

import android.view.View;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;

/**
 * Created by ZY on 17/12/27.
 */

public class SRTaskSubmitSucVH extends ZYBaseViewHolder {

    TaskSubmitSucListener listener;

    public SRTaskSubmitSucVH(TaskSubmitSucListener listener) {
        this.listener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                listener.onSubmiSucClick();
            }
        });
    }

    @Override
    public void updateView(Object data, int position) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_submit_suc;
    }

    public interface TaskSubmitSucListener {
        void onSubmiSucClick();
    }
}
