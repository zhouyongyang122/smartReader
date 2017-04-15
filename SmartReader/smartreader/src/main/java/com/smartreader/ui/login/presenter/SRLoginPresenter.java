package com.smartreader.ui.login.presenter;

import android.text.TextUtils;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.event.SREventLogin;
import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.login.contract.SRLoginContract;
import com.smartreader.ui.login.model.SRLoginModel;
import com.smartreader.ui.login.model.SRUserManager;
import com.smartreader.ui.login.model.bean.SRUser;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by ZY on 17/4/4.
 */

public class SRLoginPresenter extends ZYBasePresenter implements SRLoginContract.IPresenter {

    SRLoginContract.IView iView;

    SRLoginModel model;

    public SRLoginPresenter(SRLoginContract.IView iView) {
        this.iView = iView;
        iView.setPresenter(this);
        model = new SRLoginModel();
    }

    @Override
    public void login(String mobile, String pwd) {
        iView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(model.login(mobile, pwd), new ZYNetSubscriber<ZYResponse<SRUser>>() {
            @Override
            public void onSuccess(ZYResponse<SRUser> response) {
                iView.hideProgress();
                super.onSuccess(response);
                if (response.data != null) {
                    SRUser user = response.data;
                    user.isLoginUser = true;
                    if (TextUtils.isEmpty(user.nickname)) {
                        user.nickname = "还没设置昵称";
                    }
                    if (user.grade <= 0) {
                        user.grade = 1;
                    }
                    user.update();
                    SRUserManager.getInstance().setUser(user);
                    EventBus.getDefault().post(new SREventLogin());
                    iView.loginSuccess("登录成功");
                } else {
                    onFail("登录失败,请重新尝试!");
                }
            }

            @Override
            public void onFail(String message) {
                iView.hideProgress();
                super.onFail(message);
            }
        }));
    }

    @Override
    public void loginByThrid(Map<String, String> paramas) {
        iView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(model.thirdLogin(paramas), new ZYNetSubscriber<ZYResponse<SRUser>>() {
            @Override
            public void onSuccess(ZYResponse<SRUser> response) {
                iView.hideProgress();
                super.onSuccess(response);
                if (response.data != null) {
                    SRUser user = response.data;
                    user.isLoginUser = true;
                    if (TextUtils.isEmpty(user.nickname)) {
                        user.nickname = "还没设置昵称";
                    }
                    if (user.grade <= 0) {
                        user.grade = 1;
                    }
                    user.update();
                    SRUserManager.getInstance().setUser(user);
                    EventBus.getDefault().post(new SREventLogin());
                    iView.loginSuccess("登录成功");
                } else {
                    onFail("登录失败,请重新尝试!");
                }
            }

            @Override
            public void onFail(String message) {
                iView.hideProgress();
                super.onFail(message);
            }
        }));
    }
}
