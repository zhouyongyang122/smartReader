package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.activity.SRClassDetailActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.utils.ZYToast;

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

    @Bind(R.id.textClassUser)
    TextView textClassUser;

    SRClass mData;

    ClassDetailHeaderListener mListener;

    public SRClassDetailHeaderVH(ClassDetailHeaderListener listener) {
        mListener = listener;
    }

    @Override
    public void updateView(SRClass data, int position) {
        if (data != null) {
            mData = data;
        } else {
            mItemView.setVisibility(View.GONE);
        }

        if (textClassName != null && mData != null) {
            mItemView.setVisibility(View.VISIBLE);
            textSchoolName.setText("学校名称: " + mData.school_name);
            textClassName.setText("班级名称: " + mData.class_name);
            textCode.setText("班级邀请码: " + mData.code);
        }
    }

    @OnClick({R.id.textCode, R.id.textUsers, R.id.textClassUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textCode:
                ClipboardManager c = (ClipboardManager) mContext.getSystemService(mContext.CLIPBOARD_SERVICE);
                c.setPrimaryClip(ClipData.newPlainText(null, mData.code));
                ZYToast.show(mContext, "邀请码已复制到粘贴板!");
                break;
            case R.id.textUsers://班级详情
                mContext.startActivity(SRClassDetailActivity.createIntent(mContext, mData));
                break;
            case R.id.textClassUser:
                mListener.onClassChangeClick();
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_detail_header;
    }

    public interface ClassDetailHeaderListener {
        void onClassChangeClick();
    }
}
