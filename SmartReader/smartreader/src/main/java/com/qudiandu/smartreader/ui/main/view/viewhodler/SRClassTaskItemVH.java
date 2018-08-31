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
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/23.
 */

public class SRClassTaskItemVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.imgFinished)
    ImageView mImgFinished;

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

    @Bind(R.id.textTag)
    TextView textTag;

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
            textSubTitle.setText(mData.title);
            textTitle.setText(mData.unit);
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
            } else {
                textFinishNum.setVisibility(View.GONE);
            }

            switch (mData.ctype) {
                case SRTask.TASK_TYPE_RECORD:
                    textTag.setText("配音任务");
                    textTag.setTextColor(ZYResourceUtils.getColor(R.color.class_task_tag_font_1));
                    textTag.setBackgroundResource(R.drawable.sr_bg_class_task_tag_1);
                    recordTask();
                    break;
                case SRTask.TASK_TYPE_LISTEN:
                    textTag.setText("课堂录音");
                    textTag.setTextColor(ZYResourceUtils.getColor(R.color.class_task_tag_font_3));
                    textTag.setBackgroundResource(R.drawable.sr_bg_class_task_tag_3);
                    listenTask();
                    break;
                case SRTask.TASK_TYPE_PIC:
                    textTag.setText("图片题");
                    textTag.setTextColor(ZYResourceUtils.getColor(R.color.class_task_tag_font_4));
                    textTag.setBackgroundResource(R.drawable.sr_bg_class_task_tag_4);
                    picTask();
                    break;
                case SRTask.TASK_TYPE_AUDIO:
                    textTag.setText("语音题");
                    textTag.setTextColor(ZYResourceUtils.getColor(R.color.class_task_tag_font_2));
                    textTag.setBackgroundResource(R.drawable.sr_bg_class_task_tag_2);
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
            if (mData.finish.get(0).score < 60) {
                textScore.setBackgroundResource(R.drawable.unpass);
            } else {
                textScore.setBackgroundResource(R.drawable.pass);
            }
            if (mData.hasComment()) {
                changeFinished(false);
                textFinish.setText("看点评");
            } else {
                changeFinished(true);
                textFinish.setText("已完成");
            }
        } else {
            textScore.setVisibility(View.GONE);
            changeFinished(false);
            textFinish.setText("去完成");
        }
    }

    private void listenTask() {
        textScore.setVisibility(View.GONE);
        if (mData.isFinished()) {
            textFinish.setText("课程录音");
        } else {
            textFinish.setText("课程录音");
        }
    }

    private void picTask() {
        textScore.setVisibility(View.GONE);
        if (mData.isFinished()) {
            if (mData.hasComment()) {
                textFinish.setText("看点评");
                changeFinished(false);
            } else {
                changeFinished(true);
                textFinish.setText("已完成");
            }
        } else {
            changeFinished(false);
            textFinish.setText("选图答题");
        }
    }

    private void audioTask() {
        textScore.setVisibility(View.GONE);
        if (mData.isFinished()) {
            if (mData.hasComment()) {
                textFinish.setText("看点评");
                changeFinished(false);
            } else {
                changeFinished(true);
                textFinish.setText("已完成");
            }
        } else {
            changeFinished(false);
            textFinish.setText("语音答题");
        }
    }

    void changeFinished(boolean showImg) {
        if (showImg) {
            mImgFinished.setVisibility(View.VISIBLE);
            textFinish.setVisibility(View.GONE);
        } else {
            mImgFinished.setVisibility(View.GONE);
            textFinish.setVisibility(View.VISIBLE);
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
                if (mData.ctype == SRTask.TASK_TYPE_LISTEN) {
                    listener.onFinisheTask(mData);
                    return;
                }
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
