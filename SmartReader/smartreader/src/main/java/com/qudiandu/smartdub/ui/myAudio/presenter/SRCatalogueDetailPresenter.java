package com.qudiandu.smartdub.ui.myAudio.presenter;

import com.qudiandu.smartdub.SRApplication;
import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYBasePresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.service.net.ZYOkHttpNetManager;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.ui.myAudio.contract.SRCatalogueDetailContract;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueDetail;
import com.qudiandu.smartdub.ui.myAudio.model.SRMyAudioModel;

import java.io.File;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCatalogueDetailPresenter extends ZYBasePresenter implements SRCatalogueDetailContract.IPresenter {

    SRCatalogueDetailContract.IView mView;

    String show_id;

    SRMyAudioModel mModel;

    SRCatalogueDetail mCatalogueDetail;

    public SRCatalogueDetailPresenter(SRCatalogueDetailContract.IView view, String show_id) {
        mView = view;
        this.show_id = show_id;
        mView.setPresenter(this);
        mModel = new SRMyAudioModel();
    }

    public void loadData() {
        mView.showLoading();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getCatalogueDetail(show_id), new ZYNetSubscriber<ZYResponse<SRCatalogueDetail>>() {
            @Override
            public void onSuccess(ZYResponse<SRCatalogueDetail> response) {
                super.onSuccess(response);
                if (response.data != null) {
                    mCatalogueDetail = response.data;
                    final String filePath = SRApplication.MY_AUDIO_ROOT_DIR + mCatalogueDetail.getBook_id()+ "_" + mCatalogueDetail.getCatalogue_id() + "_" + mCatalogueDetail.getId();
                    mCatalogueDetail.setAudioPath(filePath);
                    if (!new File(filePath).exists()) {
                        ZYOkHttpNetManager.getInstance().downloadFile(mCatalogueDetail.getAudio(), filePath, new ZYOkHttpNetManager.OkHttpNetListener() {
                            @Override
                            public void onFailure(String message) {
                                new File(filePath).delete();
                                SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onFail(null);
                                    }
                                });
                            }

                            @Override
                            public void onSuccess(Object response) {
                                SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.hideLoading();
                                        mView.showCatalogueData(mCatalogueDetail);
                                    }
                                });
                            }
                        });
                    } else {
                        mView.hideLoading();
                        mView.showCatalogueData(mCatalogueDetail);
                    }
                } else {
                    onFail(null);
                }
            }

            @Override
            public void onFail(String message) {
                mView.hideLoading();
                mView.showError();
            }
        }));
    }

    @Override
    public void onSelecteTrack(SRTract tract) {

    }

    public SRCatalogueDetail getCatalogueDetail() {
        return mCatalogueDetail;
    }
}
