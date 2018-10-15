package com.qudiandu.smartreader.ui.invite;

import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.utils.ZYDateUtils;

import butterknife.Bind;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 下午9:06$
 */
public class SRInviteInfoVH extends ZYBaseViewHolder<SRInviteInfo> {

    @Bind(R.id.textPhone)
    TextView textPhone;

    @Bind(R.id.textMoney)
    TextView textMoney;

    @Bind(R.id.textInfo)
    TextView textInfo;

    @Bind(R.id.textTime)
    TextView textTime;

    @Override
    public void updateView(SRInviteInfo data, int position) {

        if (data != null) {
            textPhone.setText(data.mobile);
            textMoney.setText(data.remark);
            textInfo.setText(data.info);
            textTime.setText(ZYDateUtils.getTimeString(data.create_time, ZYDateUtils.YYMMDDHHMMSS24));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_invite_info_item;
    }
}
