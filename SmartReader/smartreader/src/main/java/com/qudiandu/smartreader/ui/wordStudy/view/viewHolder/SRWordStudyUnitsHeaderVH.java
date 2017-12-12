package com.qudiandu.smartreader.ui.wordStudy.view.viewHolder;

import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyUnitsHeaderVH extends ZYBaseViewHolder<SRBook> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textGrade)
    TextView textGrade;

    SRBook mData;

    @Override
    public void updateView(SRBook data, int position) {
        if(data != null){
            mData= data;
        }
        if (mData != null && imgBg != null) {
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this,imgBg,data.getPic(), ZYScreenUtils.dp2px(mContext,6));
            textName.setText(data.getName());
            textGrade.setText(data.getGrade());
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_word_study_units_header;
    }
}
