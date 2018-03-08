package com.qudiandu.smartreader.ui.vip.view.viewHolder;

import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.vip.model.bean.SRVip;

import butterknife.Bind;

/**
 * Created by ZY on 18/3/6.
 */

public class SRVipRightsVH extends ZYBaseViewHolder<SRVip.Rights> {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    SRVip.Rights mData;

    @Override
    public void updateView(SRVip.Rights data, int position) {
        if (data != null) {
            mData = data;
            imgAvatar.setImageResource(data.res);
            textName.setText(data.title);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fz_view_vip_rights;
    }
}
