package com.smartreader.ui.profile.presenter;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.event.SREventEditSuc;
import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.login.model.SRUserManager;
import com.smartreader.ui.login.model.bean.SRUser;
import com.smartreader.ui.profile.contact.SREditContract;
import com.smartreader.ui.profile.model.SREditModel;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by ZY on 17/4/7.
 */

public class SREditPresenter extends ZYBasePresenter implements SREditContract.IPresenter {

    SREditContract.IView iView;

    SREditModel model;

    public SREditPresenter(SREditContract.IView iView) {
        this.iView = iView;
        iView.setPresenter(this);
        model = new SREditModel();
    }

    public void editUser(Map<String, String> params) {
        iView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(model.editUser(params), new ZYNetSubscriber<ZYResponse<SRUser>>() {
            @Override
            public void onSuccess(ZYResponse<SRUser> response) {
                iView.hideProgress();
                super.onSuccess(response);
                iView.showToast("修改成功");
                if (response.data != null) {
                    SRUserManager.getInstance().setUser(response.data);
                }
                EventBus.getDefault().post(new SREventEditSuc());
                iView.finish();
            }

            @Override
            public void onFail(String message) {
                iView.hideProgress();
                super.onFail(message);
            }
        }));
    }

}