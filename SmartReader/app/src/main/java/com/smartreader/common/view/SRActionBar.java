package com.smartreader.common.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartreader.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/15.
 */

public class SRActionBar extends RelativeLayout {

    @Bind(R.id.tvLeftTitle)
    public TextView mTvLeftTile;

    @Bind(R.id.ivLeftImg)
    public ImageView mIvLeftImg;

    @Bind(R.id.tvCenterTitle)
    public TextView mTvCenterTile;

    @Bind(R.id.tvRightTitle)
    public TextView mTvRightTile;

    @Bind(R.id.ivRightImg)
    public ImageView mIvRightImg;

    public SRActionBar(Context context) {
        super(context);
        init(context);
    }

    public SRActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SRActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SRActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.sr_view_base_action_bar, null);
        addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);
    }

    public void showTitle(String title) {
        if (title == null || "".equals(title)) {
            return;
        }
        mTvCenterTile.setText(title);
    }

    public void hideActionLeftImg() {
        mIvLeftImg.setVisibility(GONE);
    }

    public void showActionLeftTitle(String title, OnClickListener clickListener) {
        if (title == null || "".equals(title)) {
            return;
        }
        mIvLeftImg.setVisibility(GONE);
        mTvLeftTile.setVisibility(VISIBLE);
        mTvLeftTile.setText(title);
        mTvLeftTile.setOnClickListener(clickListener);
    }

    public void showActionRightTitle(String title, OnClickListener clickListener) {
        if (title == null || "".equals(title)) {
            return;
        }
        mIvRightImg.setVisibility(GONE);
        mTvRightTile.setVisibility(VISIBLE);
        mTvRightTile.setText(title);
        mTvRightTile.setOnClickListener(clickListener);
    }

    public void showActionRightImg(int res, OnClickListener clickListener) {
        if (res <= 0) {
            return;
        }
        mIvRightImg.setVisibility(VISIBLE);
        mTvRightTile.setVisibility(GONE);
        mIvRightImg.setImageResource(res);
        mIvRightImg.setOnClickListener(clickListener);
    }

    public void onDestory() {
        try {
            ButterKnife.unbind(this);
        } catch (Exception e) {

        }
    }
}
