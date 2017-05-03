package com.smartreader.ui.login.presenter;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.login.contract.SRRegisterContract;
import com.smartreader.ui.login.model.SRLoginModel;
import com.smartreader.ui.login.model.bean.SRUser;

import rx.Observable;

/**
 * Created by ZY on 17/4/4.
 */

public class SRRegisterPresenter extends ZYBasePresenter implements SRRegisterContract.IPresenter {

    //注册
    public static final int REGISTER_TYPE = 0;

    //找回密码
    public static final int FORGET_TYPE = 1;

    //绑定手机号码
    public static final int BIND_TYPE = 2;

    //修改密码
    public static final int CHANGE_PWD_TYPE = 3;

    SRRegisterContract.IView iView;

    SRLoginModel model;

    int type;

    public SRRegisterPresenter(SRRegisterContract.IView iView, int type) {
        this.iView = iView;
        iView.setPresenter(this);
        this.type = type;
        model = new SRLoginModel();
    }

    @Override
    public void register(String mobile, String code, String pwd) {
        iView.showProgress();
        Observable<ZYResponse<SRUser>> observable = null;
        if (type == REGISTER_TYPE) {
            observable = model.register(mobile, pwd, code);
        } else if (type == FORGET_TYPE) {
            observable = model.resetPassword(mobile, pwd, code);
        } else {
            observable = model.bindMobile(mobile, pwd, code);
        }
        mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<SRUser>>() {
            @Override
            public void onSuccess(ZYResponse<SRUser> response) {
                super.onSuccess(response);
                iView.hideProgress();
                String msg = null;
                if (type == REGISTER_TYPE) {
                    msg = "注册成功!";
                } else if (type == FORGET_TYPE) {
                    msg = "密码重置成功!";
                } else {
                    msg = "手机号绑定成功!";
                }
                iView.registerSuccess(msg);
            }

            @Override
            public void onFail(String message) {
                iView.hideProgress();
                super.onFail(message);
                iView.registerError(message);
            }
        }));
    }

    @Override
    public void changePwd(String oldPwd, String newPwd) {
        iView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(model.changePassword(oldPwd, newPwd), new ZYNetSubscriber<ZYResponse<SRUser>>() {
            @Override
            public void onSuccess(ZYResponse<SRUser> response) {
                super.onSuccess(response);
                iView.hideProgress();
                iView.registerSuccess("密码修改成功");
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                iView.showProgress();
                iView.registerError(message);
            }
        }));
    }

    @Override
    public void getCode(String mobile) {
        int codeType = 1;
        if (type != REGISTER_TYPE) {
            codeType = 2;
        }
        mSubscriptions.add(ZYNetSubscription.subscription(model.mobileCode(mobile, codeType), new ZYNetSubscriber() {
            @Override
            public void onSuccess(ZYResponse response) {
                super.onSuccess(response);
                iView.codeSuccess("验证码发送成功,请等待!");
            }

            @Override
            public void onFail(String message) {
                super.onFail("验证码获取失败,请重新尝试!");
                iView.codeError("验证码获取失败,请重新尝试!");
            }
        }));
    }

    public int getType() {
        return type;
    }
}