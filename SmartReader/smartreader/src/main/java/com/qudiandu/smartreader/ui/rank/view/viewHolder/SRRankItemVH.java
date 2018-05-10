package com.qudiandu.smartreader.ui.rank.view.viewHolder;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.profile.activity.SRPersonHomeActivity;
import com.qudiandu.smartreader.ui.rank.model.SRRankModel;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;
import com.qudiandu.smartreader.ui.vip.view.SRVipIconView;
import com.qudiandu.smartreader.utils.ZYResourceUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRankItemVH extends ZYBaseViewHolder<SRRank> {

    @Bind(R.id.textRank)
    TextView textRank;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.imgRank)
    ImageView imgRank;

    @Bind(R.id.imgVip)
    ImageView imgVip;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textLesson)
    TextView textLesson;

    @Bind(R.id.textScore)
    TextView textScore;

    @Bind(R.id.textSuport)
    TextView textSuport;

    SRRank mData;

    @Override
    public void updateView(SRRank data, int position) {
        if (data != null) {
            mData = data;

            textRank.setText((position + 3) + "");
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, data.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            SRVipIconView.showVipIcon(imgVip, data.is_vip);
            textName.setText(data.nickname);
            textLesson.setText(data.unit);
            textScore.setText(data.score + "");
            setSuport();

            if (data.isVip()) {
                textName.setTextColor(ZYResourceUtils.getColor(R.color.c3));
            } else {
                textName.setTextColor(ZYResourceUtils.getColor(R.color.c6));
            }

//            if (position == 0) {
//                imgRank.setVisibility(View.VISIBLE);
//                imgRank.setImageResource(R.drawable.icon_gold);
//                textRank.setTextColor(ZYResourceUtils.getColor(R.color.yellow));
//            } else if (position == 1) {
//                imgRank.setVisibility(View.VISIBLE);
//                imgRank.setImageResource(R.drawable.icon_copper);
//                textRank.setTextColor(ZYResourceUtils.getColor(R.color.c11));
//            } else if (position == 2) {
//                imgRank.setVisibility(View.VISIBLE);
//                imgRank.setImageResource(R.drawable.icon_silver);
//                textRank.setTextColor(ZYResourceUtils.getColor(R.color.c4));
//            } else {
            imgRank.setVisibility(View.GONE);
            textRank.setTextColor(ZYResourceUtils.getColor(R.color.c7));
//            }
        }
    }

    @OnClick({R.id.textSuport, R.id.imgAvatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAvatar:
                SRUser user = new SRUser();
                user.uid = mData.uid + "";
                user.avatar = mData.avatar;
                user.nickname = mData.nickname;
                user.is_vip = mData.is_vip + "";
                mContext.startActivity(SRPersonHomeActivity.createIntent(mContext, user));
                break;
            case R.id.textSuport:
                ZYNetSubscription.subscription(new SRRankModel().support(mData.show_id + "", mData.isSupport() ? 2 : 1), new ZYNetSubscriber() {
                    @Override
                    public void onSuccess(ZYResponse response) {
                        try {
                            if (mData.isSupport()) {
                                mData.is_support = 0;
                                mData.supports--;
                            } else {
                                mData.is_support = 1;
                                mData.supports++;
                            }
                            setSuport();
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
                break;
        }
    }

    private void setSuport() {
        Drawable drawable = null;
        if (mData.isSupport()) {
            drawable = mContext.getResources().getDrawable(R.drawable.icon_praise_push);
        } else {
            drawable = mContext.getResources().getDrawable(R.drawable.icon_praise_normal);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            textSuport.setCompoundDrawables(drawable, null, null, null);
        }
        textSuport.setSelected(mData.isSupport());
        textSuport.setText(mData.supports + "");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_rank_item;
    }
}
