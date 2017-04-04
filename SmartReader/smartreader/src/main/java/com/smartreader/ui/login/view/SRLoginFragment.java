package com.smartreader.ui.login.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.ui.login.activity.SRRegisterActivity;
import com.smartreader.ui.login.contract.SRLoginContract;
import com.smartreader.ui.login.contract.SRRegisterContract;
import com.smartreader.ui.login.presenter.SRRegisterPresenter;
import com.smartreader.utils.ZYStringUtils;
import com.smartreader.utils.ZYToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/4.
 */

public class SRLoginFragment extends ZYBaseFragment<SRLoginContract.IPresenter> implements SRLoginContract.IView {

    @Bind(R.id.editMobile)
    EditText editMobile;

    @Bind(R.id.editPwd)
    EditText editPwd;

    @Bind(R.id.textLogin)
    TextView textLogin;

    @Bind(R.id.textForget)
    TextView textForget;

    @Bind(R.id.textRegister)
    TextView textRegister;

    @Bind(R.id.layoutWX)
    LinearLayout layoutWX;

    @Bind(R.id.layoutQQ)
    LinearLayout layoutQQ;

    @Bind(R.id.layoutSina)
    LinearLayout layoutSina;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_activity_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.textLogin, R.id.textForget, R.id.textRegister, R.id.layoutWX, R.id.layoutQQ, R.id.layoutSina})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textLogin:
                String mobile = editMobile.getText().toString();
                if (TextUtils.isEmpty(mobile) || mobile.length() > 11 || !ZYStringUtils.checkIsAllDigit(mobile)) {
                    ZYToast.show(mActivity, "请输入正确的手机号码");
                    return;
                }
                String pwd = editPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ZYToast.show(mActivity, "密码不能为空");
                    return;
                }
                mPresenter.login(mobile,pwd);
                break;
            case R.id.textForget:
                mActivity.startActivity(SRRegisterActivity.createIntent(mActivity, SRRegisterPresenter.FORGET_TYPE));
                break;
            case R.id.textRegister:
                mActivity.startActivity(SRRegisterActivity.createIntent(mActivity, SRRegisterPresenter.REGISTER_TYPE));
                break;
            case R.id.layoutWX:
                ZYToast.show(mActivity, "第三方平台是否注册好了?");
                break;
            case R.id.layoutQQ:
                ZYToast.show(mActivity, "第三方平台是否注册好了?");
                break;
            case R.id.layoutSina:
                ZYToast.show(mActivity, "第三方平台是否注册好了?");
                break;
        }
    }

    @Override
    public void loginSuccess(String msg) {
        finish();
    }
}
