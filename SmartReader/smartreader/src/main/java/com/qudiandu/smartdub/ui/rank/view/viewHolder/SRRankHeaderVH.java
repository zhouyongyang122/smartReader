package com.qudiandu.smartdub.ui.rank.view.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.ui.rank.model.bean.SRRank;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/27.
 */

public class SRRankHeaderVH extends ZYBaseViewHolder<List<SRRank>> {

    @Bind(R.id.layoutInfo)
    LinearLayout layoutInfo;

    @Bind(R.id.layoutEmpty)
    LinearLayout layoutEmpty;

    SRRankHeaderItemVH oneHeaderItem;

    SRRankHeaderItemVH secondHeaderItem;

    SRRankHeaderItemVH thirdHeaderItem;

    boolean mFristLoading;

    public void updateView(List<SRRank> datas, boolean isFristLoading) {
        mFristLoading = isFristLoading;
        updateView(datas, 0);
    }

    @Override
    public void updateView(List<SRRank> datas, int position) {

        if (secondHeaderItem == null && layoutInfo != null) {
            secondHeaderItem = new SRRankHeaderItemVH(SRRankHeaderItemVH.RANK_SECOND);
            secondHeaderItem.bindView(LayoutInflater.from(mContext).inflate(secondHeaderItem.getLayoutResId(), layoutInfo, false));
            layoutInfo.addView(secondHeaderItem.getItemView());
        }

        if (oneHeaderItem == null && layoutInfo != null) {
            oneHeaderItem = new SRRankHeaderItemVH(SRRankHeaderItemVH.RANK_ONE);
            oneHeaderItem.bindView(LayoutInflater.from(mContext).inflate(oneHeaderItem.getLayoutResId(), layoutInfo, false));
            layoutInfo.addView(oneHeaderItem.getItemView());
        }

        if (thirdHeaderItem == null && layoutInfo != null) {
            thirdHeaderItem = new SRRankHeaderItemVH(SRRankHeaderItemVH.RANK_THIRD);
            thirdHeaderItem.bindView(LayoutInflater.from(mContext).inflate(thirdHeaderItem.getLayoutResId(), layoutInfo, false));
            layoutInfo.addView(thirdHeaderItem.getItemView());
        }

        if (datas != null && datas.size() > 0) {
            if (layoutInfo != null) {
                layoutInfo.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.INVISIBLE);
                SRRank rankInfoOne = datas.get(0);
                SRRank rankInfoSecond = datas.size() > 1 ? datas.get(1) : null;
                SRRank rankInfoThird = datas.size() > 2 ? datas.get(2) : null;
                oneHeaderItem.updateView(rankInfoOne, 0);
                secondHeaderItem.updateView(rankInfoSecond, 0);
                thirdHeaderItem.updateView(rankInfoThird, 0);
            }
        } else {
            if (mFristLoading) {
                //如果第一次加载 则显示默认的1.2.3用户信息 不显示空数据信息
                return;
            }
            if (layoutInfo != null) {
                layoutInfo.setVisibility(View.INVISIBLE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fz_view_rank_header;
    }
}
