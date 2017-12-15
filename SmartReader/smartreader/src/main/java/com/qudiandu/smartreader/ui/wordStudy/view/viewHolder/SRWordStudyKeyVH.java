package com.qudiandu.smartreader.ui.wordStudy.view.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/12/15.
 */

public class SRWordStudyKeyVH extends ZYBaseViewHolder<String> {

    @Bind(R.id.textKey)
    TextView textKey;

    @Override
    public void findView(View view) {
        super.findView(view);
        int width = ZYScreenUtils.getScreenWidth(mContext) - ZYScreenUtils.dp2px(mContext, 20 + 15 + 5 * 7);
        width = width / 7;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textKey.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        textKey.setLayoutParams(layoutParams);
    }

    @Override
    public void updateView(String data, int position) {
        if (!TextUtils.isEmpty(data)) {
            textKey.setText(data);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_word_study_key;
    }
}
