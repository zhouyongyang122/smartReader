package com.qudiandu.smartreader.ui.main.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRClassDetailContract;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;

import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailPresenter extends ZYListDataPresenter<SRClassDetailContract.IView, SRMainModel, SRUser> implements SRClassDetailContract.IPresenter {

    String groupId;

    SRClass mClass;

    public SRClassDetailPresenter(SRClassDetailContract.IView view, String groupId) {
        super(view, new SRMainModel());
        this.groupId = groupId;
        mRows = 60;
    }

    @Override
    protected void loadData() {
        Observable<ZYResponse<List<SRUser>>> observable = null;
        if (isRefresh()) {
            observable = Observable.zip(mModel.getClassDetail(groupId), mModel.getClassUsers(groupId, mStart, mRows), new Func2<ZYResponse<SRClass>, ZYResponse<List<SRUser>>, ZYResponse<List<SRUser>>>() {
                @Override
                public ZYResponse<List<SRUser>> call(ZYResponse<SRClass> srClassZYResponse, ZYResponse<List<SRUser>> listZYResponse) {
                    mClass = srClassZYResponse.data;
                    return listZYResponse;
                }
            });
        } else {
            observable = mModel.getClassUsers(groupId, mStart, mRows);
        }
        mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<List<SRUser>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRUser>> response) {
                if (isRefresh()) {
                    mView.refreshHeader(mClass);
                }
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    public SRClass getClassDetail() {
        return mClass;
    }
}
