package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskDetailHeaderVH extends ZYBaseViewHolder<SRTask> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textUnit)
    TextView textUnit;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textProgress)
    TextView textProgress;

    @Bind(R.id.textNum)
    TextView textNum;

    SRTask mData;

    @Override
    public void updateView(SRTask data, int position) {
        if (data != null) {
            mData = data;
        }

        if (mData != null && imgBg != null) {
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, mData.page_url);
            textUnit.setText(mData.unit);
            textTitle.setText(mData.title);
            textProgress.setText("完成度 " + mData.cur_num + "/" + mData.limit_num);
            textNum.setText("已完成学员(" + mData.cur_num + ")");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_detail_header;
    }
}
