package com.qudiandu.smartreader.ui.vip.view.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.vip.model.bean.SRVip;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 18/3/6.
 */

public class SRVipPriceVH extends ZYBaseViewHolder<SRVip.Price> {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textPrice)
    TextView textPrice;

    @Bind(R.id.btnCheck)
    Button btnCheck;

    SRVip.Price mData;

    VipPriceVHListener mListener;

    public SRVipPriceVH(VipPriceVHListener listener) {
        mListener = listener;
    }

    @Override
    public void updateView(SRVip.Price data, int position) {
        if (data != null) {
            mData = data;
            textName.setText(data.desc);
            textPrice.setText(data.amount);
            btnCheck.setSelected(data.isSelected);
        }
    }

    @OnClick({R.id.btnCheck})
    public void onClick(View view) {
        mListener.onPriceClick(mData);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fz_view_vip_price;
    }

    public interface VipPriceVHListener {
        void onPriceClick(SRVip.Price price);
    }
}
