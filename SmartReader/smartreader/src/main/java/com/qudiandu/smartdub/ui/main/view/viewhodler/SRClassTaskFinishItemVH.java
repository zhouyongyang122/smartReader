package com.qudiandu.smartdub.ui.main.view.viewhodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.main.model.bean.SRTask;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/23.
 */

public class SRClassTaskFinishItemVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textSubTitle)
    TextView textSubTitle;

    @Bind(R.id.textScore)
    TextView textScore;

    @Bind(R.id.textComment)
    TextView textComment;

    @Bind(R.id.textFinish)
    TextView textFinish;

    SRTask mData;

    @Override
    public void updateView(Object data, int position) {
        if (data != null && data instanceof SRTask) {
            mData = (SRTask) data;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, mData.page_url);
            textTitle.setText(mData.unit);
            textSubTitle.setText(mData.title);
            SRTask.Finish finish = mData.finish.get(0);
            textScore.setText(finish.score + "");
            textComment.setText(finish.comment);
            textFinish.setText("已完成");
            textFinish.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_task_finish_item;
    }
}
