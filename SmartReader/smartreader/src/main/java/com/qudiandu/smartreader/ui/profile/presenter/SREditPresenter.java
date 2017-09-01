package com.qudiandu.smartreader.ui.profile.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.event.SREventIdentityChange;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.profile.contact.SREditContract;
import com.qudiandu.smartreader.ui.profile.model.SREditModel;

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
                    boolean needEvent = false;
                    if (SRUserManager.getInstance().getUser().user_type != response.data.user_type) {
                        needEvent = true;
                    }
                    SRUserManager.getInstance().setUser(response.data);

                    if (needEvent) {
                        EventBus.getDefault().post(new SREventIdentityChange());
                    }
                }
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
