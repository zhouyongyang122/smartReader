package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;

import butterknife.Bind;

public class SRBookHomeSpeedVH extends ZYBaseViewHolder {

    @Bind(R.id.speedBar)
    SRBookSpeedBar speedBar;

    CollationHomeSpeedListener mListener;

    float mSelSpeed = 0;

    public SRBookHomeSpeedVH(CollationHomeSpeedListener listener) {
        mListener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
    }

    @Override
    public void updateView(Object data, int position) {
        speedBar.setOnProgressChangedListener(new SRBookSpeedBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int index, float progress) {
                mSelSpeed = progress;
            }
        });
    }

    public void attchTo(ViewGroup viewGroup) {
        bindView(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false));
        viewGroup.addView(getItemView());
        updateView(null, 0);
    }

    public boolean isShow() {
        return mItemView.getVisibility() == View.VISIBLE;
    }

    public void hide() {
        mItemView.setVisibility(View.GONE);
        if (mSelSpeed > 0) {
            mListener.onSpeedSelect(mSelSpeed);
        }
    }

    public void show(float position) {
        mItemView.setVisibility(View.VISIBLE);
        mSelSpeed = 0;
        speedBar.setProgress(position);
        speedBar.postInvalidate();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fz_view_collation_speed_set;
    }

    public interface CollationHomeSpeedListener {
        void onSpeedSelect(float speed);
    }
}
