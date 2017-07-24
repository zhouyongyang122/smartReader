package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassUserItemVH extends ZYBaseViewHolder<SRUser> {

    @Bind(R.id.imgCheck)
    ImageView imgCheck;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Override
    public void updateView(SRUser data, int position) {
        if (data != null) {
            if (data.isCheck) {
                imgCheck.setSelected(true);
            } else {
                imgCheck.setSelected(false);
            }
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, data.avatar);
            textName.setText(data.nickname);

            if (data.uid.equals(SRUserManager.getInstance().getUser().uid)) {
                imgCheck.setVisibility(View.GONE);
            } else {
                imgCheck.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_users_item;
    }
}
