package com.qudiandu.smartdub.ui.task.view.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.main.model.bean.SRTask;
import com.qudiandu.smartdub.ui.task.model.SRTaskModel;
import com.qudiandu.smartdub.ui.task.model.bean.SRTaskFinish;
import com.qudiandu.smartdub.utils.ZYDateUtils;

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

    boolean mShowComment;

    int mPosition;

    public SRTaskDetailItemVH(TaskDetailItemListener listener, boolean showComment) {
        this.listener = listener;
        mShowComment = showComment;
    }


    @Override
    public void updateView(SRTaskFinish data, int position) {
        if (data != null) {
            mData = data;
            mPosition = position;
            if (mData.task_id <= 0) {
                mItemView.setVisibility(View.GONE);
                return;
            } else {
                mItemView.setVisibility(View.VISIBLE);
            }
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, mData.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(mData.nickname);
            textTime.setText(ZYDateUtils.getTimeString(Long.parseLong(mData.create_time) * 1000, ZYDateUtils.MMDDHHMM12));

            if (mData.show_id <= 0) {
                textScore.setVisibility(View.GONE);
            } else {
                textScore.setVisibility(View.VISIBLE);
                textScore.setText(mData.score + "");
            }

            if (!mShowComment) {
                textComment.setVisibility(View.GONE);
            } else {
                textComment.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mData.comment)) {
                    textComment.setText("已点评");
                } else {
                    textComment.setText("点评");
                }
            }
        } else {
            mItemView.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.textComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textComment:
                listener.onCommentClick(mData, mPosition);
                break;
        }
    }

    public interface TaskDetailItemListener {
        void onCommentClick(SRTaskFinish finish, int position);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_detail_item;
    }
}
