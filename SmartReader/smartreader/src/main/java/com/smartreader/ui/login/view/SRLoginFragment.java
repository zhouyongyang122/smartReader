package com.smartreader.ui.login.view;

import android.os.Bundle;
import android.support.annotation.MainThread;
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
import com.smartreader.thirdParty.qq.TencentManager;
import com.smartreader.thirdParty.sina.SinaManager;
import com.smartreader.thirdParty.weChat.EventWeChatAuthor;
import com.smartreader.thirdParty.weChat.WeChatManager;
import com.smartreader.ui.login.activity.SRRegisterActivity;
import com.smartreader.ui.login.contract.SRLoginContract;
import com.smartreader.ui.login.model.bean.SRThridLoginParamas;
import com.smartreader.ui.login.presenter.SRRegisterPresenter;
import com.smartreader.ui.web.SRWebViewActivity;
import com.smartreader.utils.ZYStringUtils;
import com.smartreader.utils.ZYToast;
import com.smartreader.utils.ZYUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    SRThridLoginParamas thridLoginParamas = new SRThridLoginParamas();

    boolean thirdLoginComplete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_login, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        WeChatManager.getInstance().initLogin(mActivity);

        SinaManager.getInstance().initLogin(mActivity);

        TencentManager.getInstance().initLogin(mActivity);
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
                mPresenter.login(mobile, pwd);
                break;
            case R.id.textForget:
                mActivity.startActivity(SRRegisterActivity.createIntent(mActivity, SRRegisterPresenter.FORGET_TYPE));
                break;
            case R.id.textRegister:
                mActivity.startActivity(SRRegisterActivity.createIntent(mActivity, SRRegisterPresenter.REGISTER_TYPE));
                break;
            case R.id.layoutWX:
                if (!ZYUtils.isNetworkAvailable(mActivity)) {
                    ZYToast.show(mActivity, "网络不可用...");
                    return;
                }
                if (thirdLoginComplete) {
                    thirdLoginComplete = false;

                    if (WeChatManager.getInstance().getLoginApi() == null) {
                        thirdLoginComplete = true;
                        ZYToast.show(mActivity, "微信登录出错，请退出App,重新尝试");
                        return;
                    }

                    //微信没有安装
                    if (!WeChatManager.getInstance().sendWeChatAuthRequest()) {
                        thirdLoginComplete = true;
                        ZYToast.show(mActivity, "没有安装微信App");
                    }

                } else {
                    ZYToast.show(mActivity, "请稍等,正在跳转第三方登录页面...");
                }
                break;
            case R.id.layoutQQ:

                if (!ZYUtils.isNetworkAvailable(mActivity)) {
                    ZYToast.show(mActivity, "网络不可用...");
                    return;
                }

                if (thirdLoginComplete) {
                    thirdLoginComplete = false;

                    if (TencentManager.getInstance().getmLoginTencent() == null) {
                        thirdLoginComplete = true;
                        ZYToast.show(mActivity, "QQ登录出错，请退出App,重新尝试");
                        return;
                    }

                    TencentManager.getInstance().login(mActivity, new TencentManager.TencentLoginListener() {
                        @Override
                        public void onCancel(String msg) {
                            thirdLoginComplete = true;
                            ZYToast.show(mActivity, msg);
                        }

                        @Override
                        public void onError(String msg) {
                            thirdLoginComplete = true;
                            ZYToast.show(mActivity, msg);
                        }

                        @Override
                        public void onSuccess(final SRThridLoginParamas loginParamas) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    thirdLoginComplete = true;
                                    mPresenter.loginByThrid(loginParamas.getParamas());
                                }
                            });
                        }
                    });
                } else {
                    ZYToast.show(mActivity, "请稍等,正在跳转第三方登录页面...");
                }
                break;
            case R.id.layoutSina:
                if (!ZYUtils.isNetworkAvailable(mActivity)) {
                    ZYToast.show(mActivity, "网络不可用...");
                    return;
                }
                if (thirdLoginComplete) {
                    thirdLoginComplete = false;

                    if (SinaManager.getInstance().getLoginHandler() == null) {
                        thirdLoginComplete = true;
                        ZYToast.show(mActivity, "微博登录出错，请退出App,重新尝试");
                        return;
                    }

                    SinaManager.getInstance().login(mActivity, new SinaManager.SinaLoginListener() {
                        @Override
                        public void onCancel(String msg) {
                            thirdLoginComplete = true;
                            ZYToast.show(mActivity, msg);
                        }

                        @Override
                        public void onError(String msg) {
                            thirdLoginComplete = true;
                            ZYToast.show(mActivity, msg);
                        }

                        @Override
                        public void onSuccess(final SRThridLoginParamas loginParamas) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    thirdLoginComplete = true;
                                    mPresenter.loginByThrid(loginParamas.getParamas());
                                }
                            });
                        }
                    });

                } else {
                    ZYToast.show(mActivity, "请稍等,正在跳转第三方登录页面...");
                }
                break;
        }
    }

    @Override
    public void loginSuccess(String msg) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        thirdLoginComplete = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weChatAuthResponse(EventWeChatAuthor weChatAuthor) {
        if (weChatAuthor.mUserInfo != null) {
            //成功
            thridLoginParamas.setToken(weChatAuthor.mUserInfo.openid);
            thridLoginParamas.setNickname(weChatAuthor.mUserInfo.nickname);
            thridLoginParamas.setSex(weChatAuthor.mUserInfo.sex);
            thridLoginParamas.setAuth_url(WeChatManager.getInstance().getAuth_url());
            thridLoginParamas.setAvatar(weChatAuthor.mUserInfo.headimgurl);
            thridLoginParamas.setType(SRThridLoginParamas.TYPE_WECHAT);
            mPresenter.loginByThrid(thridLoginParamas.getParamas());
        } else {
            //失败
            thirdLoginComplete = true;
        }
    }
}
