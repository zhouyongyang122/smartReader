package com.qudiandu.smartreader.ui.login.presenter;

import android.text.TextUtils;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.ZYPreferenceHelper;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.event.SREventLogin;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetManager;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.contract.SRLoginContract;
import com.qudiandu.smartreader.ui.login.model.SRLoginModel;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.utils.ZYLog;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ZY on 17/4/4.
 */

public class SRLoginPresenter extends ZYBasePresenter implements SRLoginContract.IPresenter {

    SRLoginContract.IView iView;

    SRLoginModel model;

    boolean mNeedGoVip;

    public SRLoginPresenter(SRLoginContract.IView iView) {
        this.iView = iView;
        iView.setPresenter(this);
        model = new SRLoginModel();
    }

    public SRLoginPresenter(SRLoginContract.IView iView,boolean needGoVip) {
        this.iView = iView;
        iView.setPresenter(this);
        model = new SRLoginModel();
        mNeedGoVip = needGoVip;
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
                    SRUserManager.getInstance().setUser(user);
                    EventBus.getDefault().post(new SREventLogin());
                    iView.loginSuccess("登录成功");
                    uploadJpushId();
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
                    uploadJpushId();
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

    void uploadJpushId() {
        if (!ZYPreferenceHelper.getInstance().hasUploadJPushId()) {
            mSubscriptions.add(ZYNetSubscription.subscription(ZYNetManager.shareInstance().getApi().pushInfo(JPushInterface.getRegistrationID(SRApplication.getInstance().getCurrentActivity())), new ZYNetSubscriber() {
                @Override
                public void onSuccess(ZYResponse response) {
                    ZYLog.e(getClass().getSimpleName(), "Jpush-id上传成功...............");
                    ZYPreferenceHelper.getInstance().setUploadJPushId(true);
                }

                @Override
                public void onFail(String message) {

                }
            }));
        }
    }

    public boolean isNeedGoVip() {
        return mNeedGoVip;
    }
}
