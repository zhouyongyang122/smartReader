package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.event.SREventSelectedBook;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.activity.SRMainActivity;
import com.qudiandu.smartreader.ui.main.model.SRAddBookManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.utils.ZYToast;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

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

    boolean isTeacher;

    ClassTaskItemListener listener;

    public SRClassTaskItemVH(boolean isTeacher, ClassTaskItemListener listener) {
        this.isTeacher = isTeacher;
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {
        if (data != null && data instanceof SRTask) {
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

            if (isTeacher) {
                textFinishNum.setVisibility(View.VISIBLE);
                textFinishNum.setText("完成度 " + mData.cur_num + "/" + mData.limit_num);
                textFinish.setVisibility(View.GONE);
            } else {
                textFinish.setVisibility(View.VISIBLE);
            }

            if(mData.finish != null && mData.finish.size() > 0){
                textScore.setVisibility(View.VISIBLE);
                textScore.setText(mData.finish.get(0).score + "");
            }else {
                textScore.setVisibility(View.GONE);
            }
        } else {
            mItemView.setVisibility(View.GONE);
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
                listener.onFinisheTask(mData);
                break;
        }
    }

    public interface ClassTaskItemListener {
        void onFinisheTask(SRTask task);
    }
}
