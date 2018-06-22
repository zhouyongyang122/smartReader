package com.qudiandu.smartdub.ui.myAudio.view.viewHolder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartdub.ui.rank.model.SRRankModel;
import com.qudiandu.smartdub.utils.ZYDateUtils;
import com.qudiandu.smartdub.utils.ZYScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCataloguesVH extends ZYBaseViewHolder<SRCatalogueNew> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textUnit)
    TextView textUnit;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.textScore)
    TextView textScore;

    @Bind(R.id.imgCheck)
    ImageView imgCheck;

    @Bind(R.id.textSuport)
    TextView textSuport;

    SRCatalogueNew mData;

    @Override
    public void updateView(SRCatalogueNew data, int position) {
        if (data != null) {
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, data.getPage_url(), R.drawable.img_default_pic, R.drawable.img_default_pic);
            textUnit.setText(data.getUnit());
            textTitle.setText(data.getTitle());

            textTime.setText(ZYDateUtils.getTimeString(data.getCreate_time() * 1000, "MM月dd日 HH:mm"));

            if (data.getScore() >= 60) {
                textScore.setVisibility(View.VISIBLE);
                textScore.setBackgroundResource(R.drawable.pass);
                textScore.setText(data.getScore() + "");
            } else if (data.getScore() > 0 && data.getScore() < 60) {
                textScore.setVisibility(View.VISIBLE);
                textScore.setBackgroundResource(R.drawable.unpass);
                textScore.setText(data.getScore() + "");
            } else {
                textScore.setVisibility(View.GONE);
            }

            setSuport();

            if (data.isEdit()) {
                imgCheck.setVisibility(View.VISIBLE);
                imgCheck.setSelected(mData.isSeleted);
            } else {
                imgCheck.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.textSuport})
    public void onClick(View view){
        ZYNetSubscription.subscription(new SRRankModel().support(mData.getId() + "", mData.isSupport() ? 2 : 1), new ZYNetSubscriber() {
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
        textSuport.setText(mData.getSupports() + "");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_catalogue_item;
    }
}
