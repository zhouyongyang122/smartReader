package com.qudiandu.smartreader.ui.main.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRClassUsersContract;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassUsersPresenter extends ZYListDataPresenter<SRClassUsersContract.IView, SRMainModel, SRUser> implements SRClassUsersContract.IPresenter {

    String groupId;

    public SRClassUsersPresenter(SRClassUsersContract.IView view, String groupId) {
        super(view, new SRMainModel());
        this.groupId = groupId;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getClassUsers(groupId, mStart, mRows), new ZYNetSubscriber<ZYResponse<List<SRUser>>>() {
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

    public void removeUsers() {
        String del_uid = "";
        final ArrayList<SRUser> removeUsers = new ArrayList<SRUser>();
        for (SRUser user : mDataList) {
            if (user.isCheck) {
                del_uid += user.uid + ",";
                removeUsers.add(user);
            }
        }
        if (del_uid != null) {
            mView.showProgress();
            mSubscriptions.add(ZYNetSubscription.subscription(mModel.removeUsers(groupId, del_uid), new ZYNetSubscriber() {
                @Override
                public void onSuccess(ZYResponse response) {
                    mDataList.removeAll(removeUsers);
                    mView.hideProgress();
                    mView.delUserSuc();
                }

                @Override
                public void onFail(String message) {
                    mView.hideProgress();
                    super.onFail(message);
                }
            }));
        } else {
            mView.showToast("没有选择任何学生!");
        }
    }
}
