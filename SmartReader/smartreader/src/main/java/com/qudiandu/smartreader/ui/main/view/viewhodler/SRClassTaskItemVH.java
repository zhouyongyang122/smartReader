package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.task.activity.SRTaskCommentedActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/23.
 */

public class SRClassTaskItemVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textSubTitle)
    TextView textSubTitle;

    @Bind(R.id.textFinish)
    TextView textFinish;

    @Bind(R.id.layoutDel)
    RelativeLayout layoutDel;

    @Bind(R.id.textFinishNum)
    TextView textFinishNum;

    @Bind(R.id.textScore)
    TextView textScore;

    SRTask mData;

    ClassTaskItemListener listener;

    int mPosition;

    public SRClassTaskItemVH(ClassTaskItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {
        if (data != null && data instanceof SRTask) {
            mPosition = position;
            mItemView.setVisibility(View.VISIBLE);
            mData = (SRTask) data;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, mData.page_url);
            textTitle.setText(mData.unit);
            textSubTitle.setText(mData.title);
            textFinish.setVisibility(View.VISIBLE);
            if (mData.isEdit) {
                layoutDel.setVisibility(View.VISIBLE);
            } else {
                layoutDel.setVisibility(View.GONE);
            }

            if (SRUserManager.getInstance().getUser().isTeacher()) {
                textFinishNum.setVisibility(View.VISIBLE);
                textFinishNum.setText("完成度 " + mData.cur_num + "/" + mData.limit_num);
                textFinish.setVisibility(View.GONE);
            }

            switch (mData.ctype) {
                case SRTask.TASK_TYPE_RECORD:
                    recordTask();
                    break;
                case SRTask.TASK_TYPE_LISTEN:
                    listenTask();
                    break;
                case SRTask.TASK_TYPE_PIC:
                    picTask();
                    break;
                case SRTask.TASK_TYPE_AUDIO:
                    audioTask();
                    break;
            }


        } else {
            mItemView.setVisibility(View.GONE);
        }
    }

    private void recordTask() {
        if (mData.isFinished()) {
            textScore.setVisibility(View.VISIBLE);
            textScore.setText(mData.finish.get(0).score + "");
            if (mData.hasComment()) {
                textFinish.setText("看点评");
            } else {
                textFinish.setText("已完成");
            }
        } else {
            textScore.setVisibility(View.GONE);
            textFinish.setText("去完成");
        }
    }

    private void listenTask() {
        textScore.setVisibility(View.GONE);
        if (mData.isFinished()) {
            textFinish.setText("已完成");
        } else {
            textFinish.setText("课程录音");
        }
    }

    private void picTask() {
        textScore.setVisibility(View.GONE);
        if (mData.isFinished()) {
            if (mData.hasComment()) {
                textFinish.setText("看点评");
            } else {
                textFinish.setText("已完成");
            }
        } else {
            textFinish.setText("选图答题");
        }
    }

    private void audioTask() {
        textScore.setVisibility(View.GONE);
        if (mData.isFinished()) {
            if (mData.hasComment()) {
                textFinish.setText("看点评");
            } else {
                textFinish.setText("已完成");
            }
        } else {
            textFinish.setText("语音答题");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_task_item;
    }

    @OnClick({R.id.textFinish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textFinish:
                if (mData.finish != null && mData.finish.size() > 0) {
                    if (!TextUtils.isEmpty(mData.finish.get(0).comment)) {
                        mContext.startActivity(SRTaskCommentedActivity.createIntent(mContext, mData));
                        return;
                    }
                    return;
                }
                listener.onFinisheTask(mData);
                break;
        }
    }

    public interface ClassTaskItemListener {
        void onFinisheTask(SRTask task);
    }
}
