package com.qudiandu.smartdub.ui.wordStudy.view.viewHolder;

import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.ui.wordStudy.model.bean.SRWordStudyUnit;

import butterknife.Bind;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyUnitsItemVH extends ZYBaseViewHolder<SRWordStudyUnit> {

    @Bind(R.id.textName)
    TextView textName;

    @Override
    public void updateView(SRWordStudyUnit data, int position) {
        if (data != null) {
            textName.setText(data.unit);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_word_study_units_item;
    }
}
