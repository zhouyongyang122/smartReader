package com.qudiandu.smartdub.ui.profile.presenter;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.login.model.bean.SRUser;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartdub.ui.myAudio.model.SRMyAudioModel;
import com.qudiandu.smartdub.ui.profile.contact.SRPersonHomeContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 18/3/7.
 */

public class SRPersonHomePresenter extends ZYListDataPresenter<SRPersonHomeContract.IView, SRMyAudioModel, SRCatalogueNew> implements SRPersonHomeContract.IPresenter {

    public SRUser mUser;

    public boolean needGetUserInfo;

    public SRPersonHomePresenter(SRPersonHomeContract.IView view, SRUser user) {
        super(view, new SRMyAudioModel());
        mUser = user;
    }

    public SRPersonHomePresenter(SRPersonHomeContract.IView view, String uid) {
        super(view, new SRMyAudioModel());
        mUser = new SRUser();
        mUser.uid = uid;
        needGetUserInfo = true;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getCatalogues(mUser.uid, mStart, mRows), new ZYNetSubscriber<ZYResponse<List<SRCatalogueNew>>>() {

            @Override
            public void onSuccess(ZYResponse<List<SRCatalogueNew>> response) {
                if (isRefresh()) {
                    if (response.data == null || response.data.size() <= 0) {
                        SRCatalogueNew catalogueNew = new SRCatalogueNew();
                        catalogueNew.setBook_id(-1);
                        List<SRCatalogueNew> datas = new ArrayList<SRCatalogueNew>();
                        datas.add(catalogueNew);
                        response.data = datas;
                    }
                }
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    public SRUser getUser() {
        return mUser;
    }
}
