package com.qudiandu.smartreader.ui.wordStudy.view.viewHolder;

import android.text.TextUtils;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYResourceUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyListenInputWordVH extends ZYBaseViewHolder<String> {

    @Bind(R.id.textWord)
    TextView textWord;

    int mIndex;

    String mOValue;

    public SRWordStudyListenInputWordVH(int index, String oValue) {
        mIndex = index;
        mOValue = oValue;
        ZYLog.e(getClass().getSimpleName(), "OValue: " + oValue);
    }

    @Override
    public void updateView(String data, int position) {
        if (data != null) {
            textWord.setText(data);
            show(mOValue.equals(data));
        }
    }

    public void show(boolean error) {
        if (error) {
            textWord.setTextColor(ZYResourceUtils.getColor(R.color.c3));
        } else {
            textWord.setTextColor(ZYResourceUtils.getColor(R.color.c2));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_word_study_listen_input_word;
    }
}
