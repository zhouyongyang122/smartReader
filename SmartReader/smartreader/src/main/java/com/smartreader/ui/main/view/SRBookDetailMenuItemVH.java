package com.smartreader.ui.main.view;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.ui.main.model.bean.SRCatalogue;
import com.smartreader.utils.ZYResourceUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookDetailMenuItemVH extends ZYBaseViewHolder<SRCatalogue> {

    @Bind(R.id.layoutUnit)
    RelativeLayout layoutUnit;

    @Bind(R.id.textUnit)
    TextView textUnit;

    @Bind(R.id.layoutPage)
    RelativeLayout layoutPage;

    @Bind(R.id.textPageMsg)
    TextView textPageMsg;

    @Bind(R.id.textPageNum)
    TextView textPageNum;

    private int defSelPosition;

    public SRBookDetailMenuItemVH(int defSelPosition) {
        this.defSelPosition = defSelPosition;
    }

    @Override
    public void updateView(SRCatalogue data, int position) {
        if (data != null) {
            if (data.getCatalogue_id() < 0) {
                layoutUnit.setVisibility(View.VISIBLE);
                layoutPage.setVisibility(View.GONE);
                textUnit.setText(data.getUnit());
            } else {
                layoutUnit.setVisibility(View.GONE);
                layoutPage.setVisibility(View.VISIBLE);

                if (defSelPosition == position) {
                    textPageMsg.setTextColor(ZYResourceUtils.getColor(R.color.c3));
                    textPageNum.setTextColor(ZYResourceUtils.getColor(R.color.c3));
                } else {
                    textPageMsg.setTextColor(ZYResourceUtils.getColor(R.color.white));
                    textPageNum.setTextColor(ZYResourceUtils.getColor(R.color.white));
                }
                textPageMsg.setText(data.getTitle());
                textPageNum.setText("第" + data.getPage() + "页");
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_book_detail_menu_item;
    }
}
