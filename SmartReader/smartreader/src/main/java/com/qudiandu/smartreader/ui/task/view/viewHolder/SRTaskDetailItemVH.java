package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;
import com.qudiandu.smartreader.utils.ZYDateUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskDetailItemVH extends ZYBaseViewHolder<SRTaskFinish> {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.textComment)
    TextView textComment;

    @Bind(R.id.textScore)
    TextView textScore;

    SRTaskFinish mData;

    TaskDetailItemListener listener;

    public SRTaskDetailItemVH(TaskDetailItemListener listener) {
        this.listener = listener;
    }


    @Override
    public void updateView(SRTaskFinish data, int position) {
        if (data != null) {
            mData = data;
            if (mData.show_id <= 0) {
                mItemView.setVisibility(View.GONE);
                return;
            } else {
                mItemView.setVisibility(View.VISIBLE);
            }
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, mData.avatar);
            textName.setText(mData.nickname);
            textTime.setText(ZYDateUtils.getTimeString(Long.parseLong(mData.create_time) * 1000, ZYDateUtils.MMDDHHMM12));
            textScore.setText(mData.score + "");
        }else {
            mItemView.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.textComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textComment:
                listener.onCommentClick(mData);
                break;
        }
    }

    public interface TaskDetailItemListener {
        void onCommentClick(SRTaskFinish finish);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_detail_item;
    }
}
