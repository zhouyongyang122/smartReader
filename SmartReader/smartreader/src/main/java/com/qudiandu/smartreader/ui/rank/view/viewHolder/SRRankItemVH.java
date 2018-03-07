package com.qudiandu.smartreader.ui.rank.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.rank.model.SRRankModel;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;

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

            textRank.setText((position + 1) + "");
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, data.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            imgVip.setVisibility(data.isVip() ? View.VISIBLE : View.GONE);
            textName.setText(data.name);
            textLesson.setText(data.unit);
            textScore.setText(data.score + "");
            textSuport.setText(data.supports + "");
            textSuport.setSelected(data.isSupport());
        }
    }

    @OnClick({R.id.textSuport})
    public void onClick(View view) {
        ZYNetSubscription.subscription(new SRRankModel().support(mData.id + "", mData.isSupport() ? 2 : 1), new ZYNetSubscriber() {
            @Override
            public void onSuccess(ZYResponse response) {
                try {
                    if (mData.isSupport()) {
                        mData.is_support = 0;
                    } else {
                        mData.is_support = 1;
                    }
                    textSuport.setSelected(mData.isSupport());
                } catch (Exception e) {

                }
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_rank_item;
    }
}
