package com.smartreader.ui.set.view.viewHolder;

import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.ui.set.model.bean.SRSysMsg;
import com.smartreader.utils.ZYDateUtils;

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

    @Override
    public void updateView(SRSysMsg data, int position) {
        if (data != null) {
            textTitle.setText(data.title);
            textMsg.setText(data.content);
            textTime.setText(ZYDateUtils.getTimeString(data.create_time * 1000, ZYDateUtils.YYMMDDHHMM12));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_msg_item;
    }
}
