package com.qudiandu.smartdub.ui.main.view.viewhodler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.ZYPreferenceHelper;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.utils.ZYScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/5.
 */

public class SRBookHomeSetVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.speedBar)
    SRBookSpeedBar speedBar;

    @Bind(R.id.layoutClick)
    RelativeLayout layoutClick;

    @Bind(R.id.viewClick)
    View viewClick;

    @Bind(R.id.layoutTrans)
    RelativeLayout layoutTrans;

    @Bind(R.id.transClick)
    View transClick;

    BookDetailSetListener listener;

    public SRBookHomeSetVH(BookDetailSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {
        speedBar.setProgress(ZYPreferenceHelper.getInstance().getTractSpeed());
        updateTractClickBg();
        updateTractTrans();
        speedBar.setOnProgressChangedListener(new SRBookSpeedBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int index, float progress) {
                ZYPreferenceHelper.getInstance().setTractSpeed(progress);
                listener.onTractSpeedChange(progress);
            }
        });

        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    private void updateTractTrans() {
        if (ZYPreferenceHelper.getInstance().isShowTractTrans()) {
            layoutTrans.setBackgroundResource(R.drawable.sr_bg_corner360_c9_solid);
            transClick.setBackgroundResource(R.drawable.sr_bg_corner360dp_white_solid);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) transClick.getLayoutParams();
            layoutParams.leftMargin = ZYScreenUtils.dp2px(mContext, 34);
            transClick.setLayoutParams(layoutParams);
        } else {
            layoutTrans.setBackgroundResource(R.drawable.sr_bg_corner360dp_white_solid);
            transClick.setBackgroundResource(R.drawable.sr_bg_corner360_c9_solid);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) transClick.getLayoutParams();
            layoutParams.leftMargin = 0;
            transClick.setLayoutParams(layoutParams);
        }
    }

    private void updateTractClickBg() {
        if (ZYPreferenceHelper.getInstance().isShowTractBg()) {
            layoutClick.setBackgroundResource(R.drawable.sr_bg_corner360_c9_solid);
            viewClick.setBackgroundResource(R.drawable.sr_bg_corner360dp_white_solid);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewClick.getLayoutParams();
            layoutParams.leftMargin = ZYScreenUtils.dp2px(mContext, 34);
            viewClick.setLayoutParams(layoutParams);
        } else {
            layoutClick.setBackgroundResource(R.drawable.sr_bg_corner360dp_white_solid);
            viewClick.setBackgroundResource(R.drawable.sr_bg_corner360_c9_solid);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewClick.getLayoutParams();
            layoutParams.leftMargin = 0;
            viewClick.setLayoutParams(layoutParams);
        }
    }

    @OnClick({R.id.layoutClick, R.id.layoutTrans})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutTrans: {
                boolean show = !ZYPreferenceHelper.getInstance().isShowTractTrans();
                ZYPreferenceHelper.getInstance().setShowTractTrans(show);
                updateTractTrans();
                listener.onTractTransChange(show);
                break;
            }
            case R.id.layoutClick:
                boolean show = !ZYPreferenceHelper.getInstance().isShowTractBg();
                ZYPreferenceHelper.getInstance().setShowTractBg(show);
                updateTractClickBg();
                listener.onTractClickBgChange(show);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_book_detail_set;
    }

    public void attachTo(ViewGroup viewGroup) {
        bindView(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false));
        viewGroup.addView(getItemView());
    }

    public void hide() {
        if (mItemView != null) {
            mItemView.setVisibility(View.GONE);
        }
    }

    public void show() {
        mItemView.setVisibility(View.VISIBLE);
        updateView(null, 0);
    }

    public boolean isVisibility() {
        if (mItemView == null) {
            return false;
        }
        return mItemView.getVisibility() == View.VISIBLE;
    }

    public interface BookDetailSetListener {
        void onTractTransChange(boolean show);

        void onTractClickBgChange(boolean show);

        void onTractSpeedChange(float speed);
    }
}
