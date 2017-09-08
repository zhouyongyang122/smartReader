package com.qudiandu.smartreader.ui.book.view.viewHolder;

import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.bean.SRCatalogue;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;

import butterknife.Bind;

/**
 * Created by ZY on 17/9/7.
 */

public class SRBookUnitsItemVH extends ZYBaseViewHolder<SRCatalogue> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textUnit)
    TextView textUnit;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Override
    public void updateView(SRCatalogue data, int position) {
        if (data != null) {
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, data.getPage_url(), R.drawable.def_bg, R.drawable.def_bg);
            textUnit.setText(data.getUnit());
            textTitle.setText(data.getTitle());
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_book_units_item;
    }
}
