package com.qudiandu.smartdub.ui.wordStudy.view.viewHolder;

import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;
import com.qudiandu.smartdub.utils.ZYScreenUtils;

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
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this,imgBg,mData.getPic(), ZYScreenUtils.dp2px(mContext,6));
            textName.setText(mData.getName());
            textGrade.setText(mData.getGrade());
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_word_study_units_header;
    }
}
