package com.qudiandu.smartreader.ui.myAudio.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRCatalogue;
import com.qudiandu.smartreader.ui.myAudio.contract.SRCataloguesContract;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueDetail;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.ui.myAudio.model.SRMyAudioModel;

import java.util.List;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCataloguesPresenter extends ZYListDataPresenter<SRCataloguesContract.IView, SRMyAudioModel, SRCatalogueNew> implements SRCataloguesContract.IPresenter {

    boolean isEditing;

    public SRCataloguesPresenter(SRCataloguesContract.IView view, SRMyAudioModel model) {
        super(view, model);
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getCatalogues(SRUserManager.getInstance().getUser().uid,mStart, mRows), new ZYNetSubscriber<ZYResponse<List<SRCatalogueNew>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRCatalogueNew>> response) {
                super.onSuccess(response);

                if (response.data != null && response.data.size() > 0 && isEditing) {
                    for (SRCatalogueNew value : response.data) {
                        value.setEdit(true);
                    }
                }
                success(response);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                fail(message);
            }
        }));
    }

    @Override
    public void delCatalogues(final SRCatalogueNew data) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.delCatalogue(data.getId() + ""), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
                mView.hideProgress();
                super.onSuccess(response);
                mDataList.remove(data);
                mView.delCatalogueSuc(data);
            }

            @Override
            public void onFail(String message) {
                mView.hideProgress();
                super.onFail(message);
            }
        }));
    }

    public SRCatalogueNew getDelData() {
        for (SRCatalogueNew value : mDataList) {
            if (value.isSeleted) {
                return value;
            }
        }
        return null;
    }

    public void changeSelectedValue(int position) {
        int index = 0;
        for (SRCatalogueNew value : mDataList) {
            if (index == position) {
                value.isSeleted = true;
            } else {
                value.isSeleted = false;
            }
            index++;
        }
    }

    @Override
    public void setEdit(boolean isEditing) {
        for (SRCatalogueNew value : mDataList) {
            value.setEdit(isEditing);
        }
    }
}
