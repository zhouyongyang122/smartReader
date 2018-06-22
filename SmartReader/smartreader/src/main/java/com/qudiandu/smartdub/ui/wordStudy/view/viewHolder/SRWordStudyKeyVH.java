package com.qudiandu.smartdub.ui.wordStudy.view.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/12/15.
 */

public class SRWordStudyKeyVH extends ZYBaseViewHolder<String> {

    @Bind(R.id.textKey)
    TextView textKey;

    int mType;

    int mPosition;

    WordStudyKeyListener mListener;

    public SRWordStudyKeyVH(int type, WordStudyKeyListener listener) {
        mType = type;
        mListener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        int width = ZYScreenUtils.getScreenWidth(mContext) - ZYScreenUtils.dp2px(mContext, 6 * 10);
        width = width / 10;
        int height = (int) (width / (31.0f / 42.0f));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textKey.getLayoutParams();
        if (mType == 1) {
            layoutParams.width = width;
        } else {
            layoutParams.width = width * 2 + ZYScreenUtils.dp2px(mContext, 6);
        }
        layoutParams.height = height;
        textKey.setLayoutParams(layoutParams);

        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onWordClick(mPosition);
                }
            }
        });
    }

    @Override
    public void updateView(String data, int position) {
        mPosition = position;
        if (!TextUtils.isEmpty(data)) {
            if (data.length() > 1) {
                textKey.setTextSize(25);
            } else {
                textKey.setTextSize(15);
            }
            textKey.setText(data);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_word_study_key;
    }

    public interface WordStudyKeyListener {
        void onWordClick(int position);
    }
}
