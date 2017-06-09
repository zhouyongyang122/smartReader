package com.qudiandu.smartreader.ui.mark.view;

import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;

import butterknife.Bind;

/**
 * Created by ZY on 17/6/9.
 */

public class SRMarkFooterVH extends ZYBaseViewHolder {

    @Bind(R.id.textComplete)
    TextView textComplete;

    OnCompleteClickListener mCompleteClickListener;

    public SRMarkFooterVH(OnCompleteClickListener completeClickListener) {
        mCompleteClickListener = completeClickListener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        textComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCompleteClickListener != null) {
                    mCompleteClickListener.onCompleteClick();
                }
            }
        });
    }

    @Override
    public void updateView(Object data, int position) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_mark_footer;
    }

    public interface OnCompleteClickListener {
        void onCompleteClick();
    }
}
