package com.qudiandu.smartreader.ui.profile.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;

import butterknife.Bind;

/**
 * Created by ZY on 18/3/7.
 */

public class SRPersonHomeUserVH extends ZYBaseViewHolder<SRUser> {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.imgVip)
    ImageView imgVip;

    @Bind(R.id.textGrade)
    TextView textGrade;

    SRUser mData;

    boolean isUpdate;

    @Override
    public void updateView(SRUser data, int position) {
        if (data != null) {
            mData = data;
        }
        if (mData != null && textGrade != null && !isUpdate) {
            isUpdate = true;
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, mData.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(mData.nickname);
            imgVip.setVisibility(mData.isVip() ? View.VISIBLE : View.GONE);
            textGrade.setText(mData.grade + "年级");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_person_home_user;
    }
}
