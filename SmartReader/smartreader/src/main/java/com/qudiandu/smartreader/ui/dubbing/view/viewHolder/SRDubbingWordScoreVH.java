package com.qudiandu.smartreader.ui.dubbing.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.xiansheng.XSBean;
import com.qudiandu.smartreader.utils.ZYResourceUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/12/14.
 */

public class SRDubbingWordScoreVH extends ZYBaseViewHolder<XSBean.Detail> {

    @Bind(R.id.textValue)
    TextView textValue;

    @Bind(R.id.viewPoint)
    View viewPoint;

    @Bind(R.id.textScore)
    TextView textScore;

    @Override
    public void updateView(XSBean.Detail data, int position) {
        if (data != null) {
            textValue.setText(data.value_char);
            textScore.setText(data.score < 0 ? "0" : (data.score + ""));
            if (data.score < 0) {
                viewPoint.setVisibility(View.GONE);
                textValue.setBackgroundResource(R.color.transparent);
                textValue.setTextColor(ZYResourceUtils.getColor(R.color.c6));
                textScore.setTextColor(ZYResourceUtils.getColor(R.color.c7));
            } else {
                viewPoint.setVisibility(View.VISIBLE);
                if (data.score >= 80) {
                    viewPoint.setBackgroundResource(R.drawable.label_excellent);
                    textValue.setBackgroundResource(R.drawable.sr_bg_corner4dp_c5_solid);
                    textValue.setTextColor(ZYResourceUtils.getColor(R.color.white));
                    textScore.setTextColor(ZYResourceUtils.getColor(R.color.c5));
                } else if (data.score >= 60 && data.score < 80) {
                    viewPoint.setBackgroundResource(R.drawable.label_good_2);
                    textValue.setBackgroundResource(R.drawable.sr_bg_corner4dp_c4_solid);
                    textValue.setTextColor(ZYResourceUtils.getColor(R.color.white));
                    textScore.setTextColor(ZYResourceUtils.getColor(R.color.c4));
                } else {
                    viewPoint.setBackgroundResource(R.drawable.label_fail_2);
                    textValue.setBackgroundResource(R.drawable.sr_bg_corner4dp_c3_solid);
                    textValue.setTextColor(ZYResourceUtils.getColor(R.color.white));
                    textScore.setTextColor(ZYResourceUtils.getColor(R.color.c3));
                }
            }
        } else {
            mItemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_dubbing_word_score;
    }
}
