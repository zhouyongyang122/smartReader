package com.qudiandu.smartreader.ui.main.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRClassDetailContract;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;

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
