package com.qudiandu.smartreader.ui.dubbing.view.viewHolder;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.xiansheng.XSBean;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkBean;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ZY on 17/12/1.
 */

public class SRDubbingWordVH extends ZYBaseViewHolder<SRMarkBean> {

    @Bind(R.id.textFluency)
    TextView textFluency;//流畅度

    @Bind(R.id.textIntegrity)
    TextView textIntegrity;//完整度

    @Bind(R.id.textStandard)
    TextView textStandard;//标准度

    @Bind(R.id.layoutScore)
    LinearLayout layoutScore;//单词分数

    LayoutInflater inflater;

    @Override
    public void findView(View view) {
        super.findView(view);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public void updateView(SRMarkBean data, int position) {
        if (data != null) {
            XSBean scoreBean = data.getScoreBean();
            layoutScore.removeAllViews();
            LinearLayout linearLayout = null;
            int index = 0;
            if (scoreBean != null) {
                int score = scoreBean.result.getFluency();
                textFluency.setText(setDimensionColor("流利度: " + score, score, 5, (score + "").length()));
                score = scoreBean.result.getIntegrity();
                textIntegrity.setText(setDimensionColor("完整度: " + score, score, 5, (score + "").length()));
                score = scoreBean.result.getAccuracy();
                textStandard.setText(setDimensionColor("准确度: " + score, score, 5, (score + "").length()));
                List<XSBean.Detail> details = scoreBean.result.details;
                for (XSBean.Detail detail : details) {
                    if (index % 8 == 0) {
                        linearLayout = getScoreLinear();
                        layoutScore.addView(linearLayout);
                    }
                    addScoreView(detail, linearLayout);
                    index++;
                }
            } else {
                textFluency.setText(setDimensionColor("流利度: " + 0, -1, 5, 1));
                textIntegrity.setText(setDimensionColor("完整度: " + 0, -1, 5, 1));
                textStandard.setText(setDimensionColor("准确度: " + 0, -1, 5, 1));
                for (String value : data.getValues()) {
                    if (index % 8 == 0) {
                        linearLayout = getScoreLinear();
                        layoutScore.addView(linearLayout);
                    }
                    addScoreView(new XSBean.Detail(-1, value), linearLayout);
                    index++;
                }
            }
        }
    }

    public LinearLayout getScoreLinear() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = ZYScreenUtils.dp2px(mContext, 10);
        layoutParams.leftMargin = ZYScreenUtils.dp2px(mContext, 8);
        layoutParams.rightMargin = ZYScreenUtils.dp2px(mContext, 8);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    public View addScoreView(XSBean.Detail detail, LinearLayout linearLayout) {
        SRDubbingWordScoreVH scoreVH = new SRDubbingWordScoreVH();
        scoreVH.attachTo(linearLayout);
        scoreVH.updateView(detail,0);
        return scoreVH.getItemView();
    }

    SpannableString setDimensionColor(String value, int score, int start, int len) {
        SpannableString spanText = new SpannableString(value);
        int color = 0;
        if (score < 0) {
            color = Color.parseColor("#999999");
        } else {
            if (score >= 80) {
                color = Color.parseColor("#00d365");
            } else if (score >= 60 && score < 80) {
                color = Color.parseColor("#f9b400");
            } else {
                color = Color.parseColor("#f25b6a");
            }
        }
        spanText.setSpan(new ForegroundColorSpan(color), start, start + len,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_dubbing_word;
    }
}
