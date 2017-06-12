package com.qudiandu.smartreader.ui.myAudio.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.myAudio.contract.SRCataloguesContract;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueDetail;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.ui.myAudio.model.SRMyAudioModel;

import java.util.List;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCataloguesPresenter extends ZYListDataPresenter<SRCataloguesContract.IView, SRMyAudioModel, SRCatalogueNew> implements SRCataloguesContract.IPresenter {

    public SRCataloguesPresenter(SRCataloguesContract.IView view, SRMyAudioModel model) {
        super(view, model);
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getCatalogues(mStart, mRows), new ZYNetSubscriber<ZYResponse<List<SRCatalogueNew>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRCatalogueNew>> response) {
                super.onSuccess(response);
                success(response);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                fail(message);
            }
        }));
    }
}
