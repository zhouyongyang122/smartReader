package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailItemVH extends ZYBaseViewHolder<SRUser> {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Override
    public void findView(View view) {
        super.findView(view);
        int itemWidth = (ZYScreenUtils.getScreenWidth(mContext) - ZYScreenUtils.dp2px(mContext, 10)) / 5;
        int imgWidth = itemWidth - ZYScreenUtils.dp2px(mContext, 10);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgAvatar.getLayoutParams();
        layoutParams.height = imgWidth;
        imgAvatar.setLayoutParams(layoutParams);
    }

    @Override
    public void updateView(SRUser data, int position) {
        if (data != null) {
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, data.avatar);
            textName.setText(data.nickname);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_detail_user_item;
    }
}
