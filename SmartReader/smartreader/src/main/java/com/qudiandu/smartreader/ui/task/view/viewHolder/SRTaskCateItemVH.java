package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskCate;
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

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
            imgCheck.setSelected(cate.isCheck);
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgBg, cate.page_url, ZYScreenUtils.dp2px(mContext, 6));
            textTitle.setText(cate.title);
            textSubTitle.setText(cate.unit);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_cate_item;
    }
}
