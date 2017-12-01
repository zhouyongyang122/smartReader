package com.qudiandu.smartreader.ui.dubbing.presenter;

import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.ui.dubbing.contract.SRDubbingContract;
import com.qudiandu.smartreader.ui.dubbing.model.SRDubbingModel;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;

/**
 * Created by ZY on 17/12/1.
 */

public class SRDubbingPresenter extends ZYBasePresenter implements SRDubbingContract.IPresenter {

    int mPageId;

    String mGroupId;

    SRTract mTrack;

    String mCatalogueId;

    String mTaskId;

    SRDubbingModel mModel;

    SRDubbingContract.IView mView;

    public SRDubbingPresenter(SRDubbingContract.IView iView, SRTract tract, String catalogueId, String taskId, String groupId, int pageId) {
        mModel = new SRDubbingModel();
        mGroupId = groupId;
        mTrack = tract;
        mCatalogueId = catalogueId;
        mTaskId = taskId;
        mPageId = pageId;
        mView = iView;
        mView.setPresenter(this);
    }

    @Override
    public String getGroupId() {
        return mGroupId;
    }

    public SRTract getTrack() {
        return mTrack;
    }

    public String getCatalogueId() {
        return mCatalogueId;
    }

    public String getTaskId() {
        return mTaskId;
    }

    public int getPageId() {
        return mPageId;
    }

    @Override
    public void uploadTractAudio() {

    }
}
