package com.qudiandu.smartreader.ui.set.view.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.set.model.bean.SRSysMsg;
import com.qudiandu.smartreader.utils.ZYDateUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/9.
 */

public class SRSysMsgVH extends ZYBaseViewHolder<SRSysMsg> {

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textMsg)
    TextView textMsg;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.viewPoint)
    View viewPoint;

    @Override
    public void updateView(SRSysMsg data, int position) {
        if (data != null) {
//            textTitle.setText(data.title);
            textMsg.setText(data.content);
            textTime.setText(ZYDateUtils.getTimeString(data.create_time * 1000, ZYDateUtils.YYMMDDHHMM12));
            if (!TextUtils.isEmpty(data.pic)) {
                imgBg.setVisibility(View.VISIBLE);
                ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgBg, data.pic, ZYScreenUtils.dp2px(mContext, 6), R.color.c10, R.color.c10);
            } else {
                imgBg.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_msg_item;
    }
}
