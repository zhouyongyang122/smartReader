package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.vip.view.SRVipIconView;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailItemVH extends ZYBaseViewHolder<SRUser> {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.imgVip)
    ImageView imgVip;

    @Bind(R.id.imgDel)
    ImageView imgDel;

    SRUser mData;

    ClassDetailItemListener mListener;

    public SRClassDetailItemVH(ClassDetailItemListener listener) {
        mListener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        int itemWidth = (ZYScreenUtils.getScreenWidth(mContext) - ZYScreenUtils.dp2px(mContext, 64)) / 4;
        int imgWidth = itemWidth - ZYScreenUtils.dp2px(mContext, 4);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgAvatar.getLayoutParams();
        layoutParams.height = imgWidth;
        imgAvatar.setLayoutParams(layoutParams);
    }

    @Override
    public void updateView(SRUser data, int position) {
        if (data != null) {
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, data.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(data.nickname);

            if(data.isCheck){
                imgDel.setVisibility(View.VISIBLE);
            }else {
                imgDel.setVisibility(View.GONE);
            }

            SRVipIconView.showVipIconRe(imgVip,Integer.parseInt(data.is_vip));
        }
    }

    @OnClick({R.id.imgDel})
    public void onClick(View view) {
        mListener.onDelClick(mData);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_detail_user_item;
    }

    public interface ClassDetailItemListener {
        void onDelClick(SRUser user);
    }
}
