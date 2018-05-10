package com.qudiandu.smartreader.ui.rank.view.viewHolder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartreader.ui.rank.model.SRRankModel;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/5/17.
 */

public class SRRankHeaderItemVH extends ZYBaseViewHolder<SRRank> {

    public static final int RANK_ONE = 1;

    public static final int RANK_SECOND = 2;

    public static final int RANK_THIRD = 3;

    @Bind(R.id.imgVip)
    ImageView imgVip;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textRankValue)
    TextView textRankValue;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textRank)
    TextView textRank;

    int mRankNum;

    SRRank mData;

    public SRRankHeaderItemVH(int rankNum) {
        mRankNum = rankNum;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        if (mRankNum == RANK_ONE) {
            RelativeLayout.LayoutParams vipLayoutParams = (RelativeLayout.LayoutParams) imgVip.getLayoutParams();
            vipLayoutParams.topMargin = 0;
            imgVip.setLayoutParams(vipLayoutParams);

            RelativeLayout.LayoutParams avatarLayoutParams = (RelativeLayout.LayoutParams) imgAvatar.getLayoutParams();
            avatarLayoutParams.width = ZYScreenUtils.dp2px(mContext, 76);
            avatarLayoutParams.height = avatarLayoutParams.width;
            imgAvatar.setLayoutParams(avatarLayoutParams);
        }

        switch (mRankNum) {
            case RANK_ONE:
//                textRankValue.setText("1");
                textRankValue.setBackgroundResource(R.drawable.one);
                break;
            case RANK_SECOND:
//                textRankValue.setText("2");
                textRankValue.setBackgroundResource(R.drawable.two);
                break;
            case RANK_THIRD:
//                textRankValue.setText("3");
                textRankValue.setBackgroundResource(R.drawable.three);
                break;
        }
    }

    @Override
    public void updateView(SRRank data, int position) {
        mData = data;
        if (data != null) {
            mItemView.setVisibility(View.VISIBLE);
            textName.setVisibility(View.VISIBLE);
            textRank.setVisibility(View.VISIBLE);
            textName.setText(mData.nickname);
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, mData.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            imgVip.setVisibility(mData.is_vip >= 1 ? View.VISIBLE : View.INVISIBLE);
            textName.setTextColor(mData.is_vip >= 1 ? ZYResourceUtils.getColor(R.color.c10) : ZYResourceUtils.getColor(R.color.c3));
            setSuport();
        } else {
            mItemView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fz_view_rank_header_item;
    }

    @OnClick({R.id.imgAvatar, R.id.textRank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAvatar:
                mContext.startActivity(SRCatalogueDetailActivity.createIntent(mContext, mData.show_id + ""));
                break;
            case R.id.textRank:
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
            textRank.setCompoundDrawables(drawable, null, null, null);
        }
        textRank.setSelected(mData.isSupport());
        textRank.setText(mData.supports + "");
    }
}
