package com.qudiandu.smartdub.ui.login.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.mvp.ZYBaseFragment;
import com.qudiandu.smartdub.base.view.ZYClearEditView;
import com.qudiandu.smartdub.ui.login.contract.SRRegisterContract;
import com.qudiandu.smartdub.ui.login.presenter.SRRegisterPresenter;
import com.qudiandu.smartdub.utils.ZYStringUtils;
import com.qudiandu.smartdub.utils.ZYToast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/4.
 */

public class SRRegisterFragment extends ZYBaseFragment<SRRegisterContract.IPresenter> implements SRRegisterContract.IView {

    @Bind(R.id.editMobile)
    ZYClearEditView editMobile;

    @Bind(R.id.editCode)
    ZYClearEditView editCode;

    @Bind(R.id.textCode)
    TextView textCode;

    @Bind(R.id.editPwd)
    ZYClearEditView editPwd;

    @Bind(R.id.textRegister)
    TextView textRegister;

    @Bind(R.id.layoutRegister)
    RelativeLayout layoutRegister;

    @Bind(R.id.layoutChangePwd)
    LinearLayout layoutChangePwd;

    @Bind(R.id.editOldPwd)
    ZYClearEditView editOldPwd;

    @Bind(R.id.editNewPwd)
    ZYClearEditView editNewPwd;

    private Timer timer;

    private int maxTime = 30;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_register, container, false);
        ButterKnife.bind(this, view);
        switch (mPresenter.getType()) {
            case SRRegisterPresenter.REGISTER_TYPE:
                textRegister.setText("注册账号");
                layoutChangePwd.setVisibility(View.GONE);
                layoutRegister.setVisibility(View.VISIBLE);
                break;
            case SRRegisterPresenter.FORGET_TYPE:
                textRegister.setText("重置密码");
                layoutChangePwd.setVisibility(View.GONE);
                layoutRegister.setVisibility(View.VISIBLE);
                break;
            case SRRegisterPresenter.BIND_TYPE:
                textRegister.setText("绑定手机号码");
                layoutChangePwd.setVisibility(View.GONE);
                layoutRegister.setVisibility(View.VISIBLE);
                break;
            case SRRegisterPresenter.CHANGE_PWD_TYPE:
                layoutChangePwd.setVisibility(View.VISIBLE);
                layoutRegister.setVisibility(View.GONE);
                textRegister.setText("修改密码");
                break;
        }

        return view;
    }

    @OnClick({R.id.textCode, R.id.textRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textCode: {
                String mobile = editMobile.getText().toString();
                if (TextUtils.isEmpty(mobile) || mobile.length() != 11 || !ZYStringUtils.checkIsAllDigit(mobile)) {
                    ZYToast.show(mActivity, "请输入正确的手机号码");
                    return;
                }
                textCode.setEnabled(false);
                textCode.setText("等待(" + maxTime + "s)");
                startTimer();
                mPresenter.getCode(mobile);
                break;
            }
            case R.id.textRegister: {

                if (mPresenter.getType() != SRRegisterPresenter.CHANGE_PWD_TYPE) {
                    String code = editCode.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        ZYToast.show(mActivity, "验证码不能为空!");
                        return;
                    }
                    String mobile = editMobile.getText().toString();
                    if (TextUtils.isEmpty(mobile) || mobile.length() != 11 || !ZYStringUtils.checkIsAllDigit(mobile)) {
                        ZYToast.show(mActivity, "请输入正确的手机号码");
                        return;
                    }
                    String pwd = editPwd.getText().toString().trim();
                    Pattern pattern = Pattern.compile("^[\\-a-zA-Z0-9]+$");
                    if (pwd.length() < 6 || pwd.length() > 16 || !pattern.matcher(pwd).matches()) {
                        ZYToast.show(mActivity, "密码长度必须是6-16位字母数字组合");
                        return;
                    }
//                    if (!ZYStringUtils.checkIsAllDigit(pwd)) {
//                        ZYToast.show(mActivity, "密码必须都是数字");
//                        return;
//                    }
                    mPresenter.register(mobile, code, pwd);
                } else {
                    String oldPwd = editOldPwd.getText().toString().trim();
                    if (TextUtils.isEmpty(oldPwd)) {
                        ZYToast.show(mActivity, "旧密码不能为空");
                        return;
                    }
                    String newPwd = editNewPwd.getText().toString().trim();
                    Pattern pattern = Pattern.compile("^[\\-a-zA-Z0-9]+$");
                    if (newPwd.length() < 6 || newPwd.length() > 16 || !pattern.matcher(newPwd).matches()) {
                        ZYToast.show(mActivity, "密码长度必须是6-16位字母数字组合");
                        return;
                    }
//                    if (!ZYStringUtils.checkIsAllDigit(newPwd)) {
//                        ZYToast.show(mActivity, "密码必须都是数字");
//                        return;
//                    }
                    mPresenter.changePwd(oldPwd, newPwd);
                }
                break;
            }
        }
    }

    @Override
    public void codeSuccess(String msg) {

    }

    @Override
    public void registerSuccess(String msg) {
        ZYToast.show(mActivity, msg);
        finish();
    }

    @Override
    public void registerError(String error) {

    }

    @Override
    public void codeError(String error) {
        cancleTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    editCode.post(new Runnable() {
                        @Override
                        public void run() {
                            maxTime--;
                            if (maxTime <= 0) {
                                cancleTimer();
                            } else {
                                textCode.setText("等待(" + maxTime + "s)");
                            }
                        }
                    });

                } catch (Exception e) {

                }
            }
        }, 1000, 1000);
    }

    private void cancleTimer() {
        try {
            textCode.setText("获取验证码");
            textCode.setEnabled(true);
            timer.cancel();
            timer = null;
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        cancleTimer();
        super.onDestroy();
    }
}
