package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.utils.ZYToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/9/9.
 */

public class SRClassOrganizationCodeVH extends ZYBaseViewHolder {

    @Bind(R.id.textCode)
    EditText textCode;

    @Bind(R.id.textComplete)
    TextView textComplete;

    @Bind(R.id.textSelf)
    TextView textSelf;

    ClassOrganizationCodeListener mListener;

    public SRClassOrganizationCodeVH(ClassOrganizationCodeListener listener) {
        mListener = listener;
    }

    @Override
    public void updateView(Object data, int position) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_organization_code;
    }

    @OnClick({R.id.textComplete, R.id.textSelf, R.id.ivLeftImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textSelf:
                mListener.onBackClick();
                break;
            case R.id.textComplete:
                String code = textCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    ZYToast.show(mContext, "机构邀请码不能为空!");
                    return;
                }
                mListener.onCompleteClick(code);
                break;
            case R.id.ivLeftImg:
                mListener.onBackClick();
                break;
        }
    }

    public interface ClassOrganizationCodeListener {
        void onCompleteClick(String code);

        void onBackClick();
    }
}
