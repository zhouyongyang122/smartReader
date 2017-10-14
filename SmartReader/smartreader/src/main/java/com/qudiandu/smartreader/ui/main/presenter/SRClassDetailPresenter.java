package com.qudiandu.smartreader.ui.main.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRClassDetailContract;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;
import java.util.List;
import rx.Observable;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailPresenter extends ZYListDataPresenter<SRClassDetailContract.IView, SRMainModel, SRUser> implements SRClassDetailContract.IPresenter {

    String groupId;

    public SRClassDetailPresenter(SRClassDetailContract.IView view, String groupId) {
        super(view, new SRMainModel());
        this.groupId = groupId;
        mRows = 60;
    }

    @Override
    protected void loadData() {
        Observable<ZYResponse<List<SRUser>>> observable = mModel.getClassUsers(groupId, mStart, mRows);
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
}
