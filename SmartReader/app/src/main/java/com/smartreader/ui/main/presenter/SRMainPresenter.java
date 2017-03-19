package com.smartreader.ui.main.presenter;

import com.smartreader.ui.main.model.SRMainModel;
import com.smartreader.base.mvp.SRBasePresenter;
import com.smartreader.ui.main.contract.SRMainContract;

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
