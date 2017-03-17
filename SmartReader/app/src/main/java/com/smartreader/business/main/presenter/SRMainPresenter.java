package com.smartreader.business.main.presenter;

import com.smartreader.business.main.model.SRMainModel;
import com.smartreader.common.mvp.SRBasePresenter;
import com.smartreader.business.main.contract.SRMainContract;

/**
 * Created by ZY on 17/3/16.
 */

public class SRMainPresenter extends SRBasePresenter implements SRMainContract.IPresenter {

    SRMainContract.IView mIView;

    SRMainModel mModel;

    public SRMainPresenter(SRMainContract.IView iView) {
        mIView = iView;
        mModel = new SRMainModel();
        mIView.setPresenter(this);
    }
}
