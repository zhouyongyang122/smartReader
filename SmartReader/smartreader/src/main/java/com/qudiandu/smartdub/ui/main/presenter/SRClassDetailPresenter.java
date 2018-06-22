package com.qudiandu.smartdub.ui.main.presenter;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.event.SREventUpdateClassName;
import com.qudiandu.smartdub.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.login.model.bean.SRUser;
import com.qudiandu.smartdub.ui.main.contract.SRClassDetailContract;
import com.qudiandu.smartdub.ui.main.model.SRMainModel;
import com.qudiandu.smartdub.ui.main.model.bean.SRClass;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Observable;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailPresenter extends ZYListDataPresenter<SRClassDetailContract.IView, SRMainModel, SRUser> implements SRClassDetailContract.IPresenter {

    SRClass mClass;

    public SRClassDetailPresenter(SRClassDetailContract.IView view, SRClass srClass) {
        super(view, new SRMainModel());
        mClass = srClass;
        mRows = 60;
    }

    @Override
    protected void loadData() {
        Observable<ZYResponse<List<SRUser>>> observable = mModel.getClassUsers(mClass.group_id + "", mStart, mRows);
        mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<List<SRUser>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRUser>> response) {
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    public void delUser(final SRUser user) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.removeUsers(mClass.group_id + "", user.uid), new ZYNetSubscriber() {
            @Override
            public void onSuccess(ZYResponse response) {
                mDataList.remove(user);
                mView.hideProgress();
                mView.delUserSuc();
                mClass.cur_num--;
            }

            @Override
            public void onFail(String message) {
                mView.hideProgress();
                super.onFail(message);
            }
        }));
    }

    public void updateClassName(final String name) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.updateClassName(mClass.group_id + "", name), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
                mClass.class_name = name;
                EventBus.getDefault().post(new SREventUpdateClassName(mClass));
            }

            @Override
            public void onFail(String message) {

            }
        }));
    }

    @Override
    public void edit(boolean edit) {
        for (SRUser user : getDataList()) {
            if (user.uid.equals(mClass.uid + "")) {
                continue;
            }
            user.isCheck = edit;
        }
    }

    public SRClass getMClass() {
        return mClass;
    }
}
