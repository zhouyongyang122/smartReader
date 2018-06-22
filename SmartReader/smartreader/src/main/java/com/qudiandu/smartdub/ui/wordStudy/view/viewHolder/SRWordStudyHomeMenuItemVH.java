package com.qudiandu.smartdub.ui.wordStudy.view.viewHolder;

import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.ui.wordStudy.model.bean.SRWordStudyWord;

import butterknife.Bind;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyHomeMenuItemVH extends ZYBaseViewHolder<SRWordStudyWord> {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textCnWord)
    TextView textCnWord;

    @Override
    public void updateView(SRWordStudyWord data, int position) {
        if (data != null) {
            textName.setText(data.word);
            textCnWord.setText(data.meaning);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_word_study_home_menu_item;
    }
}
