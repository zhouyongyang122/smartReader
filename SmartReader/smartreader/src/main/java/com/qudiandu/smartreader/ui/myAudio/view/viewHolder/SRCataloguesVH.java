package com.qudiandu.smartreader.ui.myAudio.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.utils.ZYDateUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;

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

            if (data.isEdit()) {
                imgCheck.setVisibility(View.VISIBLE);
                imgCheck.setSelected(mData.isSeleted);
            } else {
                imgCheck.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_catalogue_item;
    }
}
