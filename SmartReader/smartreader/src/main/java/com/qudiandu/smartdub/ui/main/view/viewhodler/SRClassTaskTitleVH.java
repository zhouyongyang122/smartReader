package com.qudiandu.smartdub.ui.main.view.viewhodler;

import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.main.model.bean.SRTask;
import com.qudiandu.smartdub.ui.main.model.bean.SRTaskTitle;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/23.
 */

public class SRClassTaskTitleVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textManager)
    TextView textManager;

    ClassTaskTitleListener listener;

    SRTaskTitle mData;

    public SRClassTaskTitleVH(ClassTaskTitleListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {
        if (data != null && data instanceof SRTaskTitle) {
            mData = (SRTaskTitle) data;
            textTitle.setText(mData.title);

            if (SRUserManager.getInstance().getUser().isTeacher()) {
                textManager.setVisibility(View.VISIBLE);
                if (mData.isEdit) {
                    textManager.setText("取消");
                } else {
                    textManager.setText("任务管理");
                }
            } else {
                textManager.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.textManager})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textManager:
                listener.onTaskManagerClick(mData.isEdit);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_task_title;
    }

    public interface ClassTaskTitleListener {
        void onTaskManagerClick(boolean isEdit);
    }
}
