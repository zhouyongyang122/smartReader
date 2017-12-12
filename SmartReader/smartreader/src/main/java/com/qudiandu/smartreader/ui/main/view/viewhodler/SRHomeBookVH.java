package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.view.View;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.book.activity.SRBookUnitsActivity;
import com.qudiandu.smartreader.ui.book.activity.SRBooksActivity;
import com.qudiandu.smartreader.ui.main.activity.SRBookHomeActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.translate.SRTranslateActivity;
import com.qudiandu.smartreader.ui.wordStudy.activity.SRWordStudyUnitsActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/27.
 */

public class SRHomeBookVH extends ZYBaseViewHolder<SRBook> {

    SRBook mData;

    @Override
    public void updateView(SRBook data, int position) {
        if (data != null) {
            mData = data;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_home_book;
    }

    @OnClick({R.id.layoutBook, R.id.layoutRead, R.id.layoutRecord, R.id.layoutPractice, R.id.layoutTranslate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBook:
                mContext.startActivity(SRBooksActivity.createIntent(mContext, 0));
                break;
            case R.id.layoutRead:
                mContext.startActivity(SRBookHomeActivity.createIntent(mContext, mData.savePath));
                break;
            case R.id.layoutRecord:
                mContext.startActivity(SRBookUnitsActivity.createIntent(mContext, mData.savePath, mData.name));
                break;
            case R.id.layoutPractice:
                mContext.startActivity(SRWordStudyUnitsActivity.createIntent(mContext, mData));
                break;
            case R.id.layoutTranslate:
                mContext.startActivity(SRTranslateActivity.createIntent(mContext));
                break;
        }
    }
}
