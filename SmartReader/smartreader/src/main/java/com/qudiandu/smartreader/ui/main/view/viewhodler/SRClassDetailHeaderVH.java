package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.activity.SRClassUsersActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailHeaderVH extends ZYBaseViewHolder<SRClass> {

    @Bind(R.id.textSchoolName)
    TextView textSchoolName;

    @Bind(R.id.textClassName)
    TextView textClassName;

    @Bind(R.id.textCode)
    TextView textCode;

    @Bind(R.id.textNum)
    TextView textNum;

    @Bind(R.id.textManager)
    TextView textManager;

    SRClass mData;

    @Override
    public void updateView(SRClass data, int position) {
        if (data != null) {
            mData = data;
        }

        if (textClassName != null && mData != null) {
            textSchoolName.setText("学校名称: " + mData.school_name);
            textClassName.setText("班级名称: " + mData.class_name);
            textNum.setText("班级学员 (" + mData.cur_num + ")");
            textCode.setText("班级邀请码: " + mData.code);

            if (SRUserManager.getInstance().getUser().uid.equals(mData.uid + "")) {
                textManager.setVisibility(View.VISIBLE);
            } else {
                textManager.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.textManager})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textManager:
                mContext.startActivity(SRClassUsersActivity.createIntent(mContext, mData.group_id + ""));
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_detail_header;
    }
}
