package com.smartreader.ui.main.view;

import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.SRApplication;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.ui.main.model.bean.SRGrade;
import com.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/31.
 */

public class SRGradeItemVH extends ZYBaseViewHolder<SRGrade> {

    @Bind(R.id.textGradeName)
    TextView textGradeName;

    private final int hSpace = ZYScreenUtils.dp2px(SRApplication.getInstance(), 5);

    @Override
    public void updateView(SRGrade data, int position) {
        if (data != null) {
            textGradeName.setText(data.grade);

            if (position == 0) {
                mItemView.setPadding(0, hSpace * 2, hSpace, 0);
            } else if (position == 1) {
                mItemView.setPadding(hSpace, hSpace * 2, 0, 0);
            } else if (position % 2 == 0) {
                mItemView.setPadding(0, 0, hSpace, 0);
            } else {
                mItemView.setPadding(hSpace, 0, 0, 0);
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_grade_item;
    }
}
