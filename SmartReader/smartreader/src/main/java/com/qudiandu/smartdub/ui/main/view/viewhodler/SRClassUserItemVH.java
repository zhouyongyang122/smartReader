package com.qudiandu.smartdub.ui.main.view.viewhodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.login.model.bean.SRUser;

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
                imgCheck.setBackgroundResource(R.drawable.sr_bg_corner360dp_boder1dpc4_solidc13);
            } else {
                imgCheck.setBackgroundResource(R.drawable.sr_bg_corner360dp_boder1dpc2_solidwhite);
            }
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, data.avatar,R.drawable.def_avatar,R.drawable.def_avatar);
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
