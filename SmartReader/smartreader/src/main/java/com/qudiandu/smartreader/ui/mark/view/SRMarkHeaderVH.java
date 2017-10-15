package com.qudiandu.smartreader.ui.mark.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.xiansheng.XSBean;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ZY on 17/10/15.
 */

public class SRMarkHeaderVH extends ZYBaseViewHolder<SRMarkBean> {

    @Bind(R.id.textFluency)
    TextView textFluency;

    @Bind(R.id.textIntegrity)
    TextView textIntegrity;

    @Bind(R.id.textStandard)
    TextView textStandard;

    @Bind(R.id.layoutRoot)
    RelativeLayout layoutRoot;

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
            layoutRoot.removeAllViews();
            LinearLayout linearLayout = null;
            int index = 0;
            int lastId = 0;
            if (scoreBean != null) {
                textFluency.setText("流利度: " + scoreBean.result.getFluency());
                textIntegrity.setText("完整度: " + scoreBean.result.getIntegrity());
                textStandard.setText("准备度: " + scoreBean.result.getAccuracy());
                List<XSBean.Detail> details = scoreBean.result.details;
                for (XSBean.Detail detail : details) {
                    if (index % 4 == 0) {
                        linearLayout = getScoreLinear(index, lastId);
                        layoutRoot.addView(linearLayout);
                        lastId++;
                    }
                    linearLayout.addView(getScoreView(index % 4, detail.value_char, detail.score));
                    index++;
                }
            } else {
                textFluency.setText("流利度: 0");
                textIntegrity.setText("完整度: 0");
                textStandard.setText("准备度: 0");
                for (String value : data.getValues()) {
                    if (index % 4 == 0) {
                        linearLayout = getScoreLinear(index, lastId);
                        layoutRoot.addView(linearLayout);
                        lastId++;
                    }
                    linearLayout.addView(getScoreView(index % 4, value, 0));
                    index++;
                }
            }
        }
    }

    public LinearLayout getScoreLinear(int index, int lastId) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setId(100 + lastId);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (index > 0) {
            layoutParams.topMargin = ZYScreenUtils.dp2px(mContext, 10);
            layoutParams.addRule(RelativeLayout.BELOW, 100 + lastId - 1);
        }
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    public TextView getScoreView(int col, String text, int score) {
        TextView textView = (TextView) inflater.inflate(R.layout.sr_view_mark_header_item, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (col > 0) {
            layoutParams.leftMargin = ZYScreenUtils.dp2px(mContext, 10);
        }
        if (score == 0) {
            textView.setBackgroundResource(R.drawable.sr_bg_corner360dp_boder_c2);
        } else if (score <= 60) {
            textView.setBackgroundResource(R.drawable.sr_bg_corner360_boder_c10);
        } else {
            textView.setBackgroundResource(R.drawable.sr_bg_corner360_boder_c13);
        }
        textView.setLayoutParams(layoutParams);
        textView.setText(text + " " + score);
        return textView;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_mark_header;
    }
}
