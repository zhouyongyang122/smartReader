package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskCate;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskCateItemVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textSubTitle)
    TextView textSubTitle;

    @Bind(R.id.imgCheck)
    ImageView imgCheck;

    @Override
    public void updateView(Object data, int position) {
        if (data != null && data instanceof SRTaskCate) {
            SRTaskCate cate = (SRTaskCate) data;
            if (cate.isCheck) {
                imgCheck.setBackgroundResource(R.drawable.sr_bg_corner360dp_boder1dpc4_solidc13);
            } else {
                imgCheck.setBackgroundResource(R.drawable.sr_bg_corner360dp_boder1dpc2_solidwhite);
            }

            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, cate.page_url);
            textTitle.setText(cate.title);
            textSubTitle.setText(cate.unit);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_cate_item;
    }
}
