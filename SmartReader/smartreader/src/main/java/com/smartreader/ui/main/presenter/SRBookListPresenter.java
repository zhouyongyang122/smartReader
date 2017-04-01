package com.smartreader.ui.main.presenter;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYListDataContract;
import com.smartreader.base.mvp.ZYListDataPresenter;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.main.contract.SRBookListContract;
import com.smartreader.ui.main.model.SRMainModel;
import com.smartreader.ui.main.model.bean.SRBook;

import java.util.List;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookListPresenter extends ZYListDataPresenter<SRBookListContract.IView, SRMainModel, SRBook> implements SRBookListContract.IPresenter {

    private String gradeId;

    public SRBookListPresenter(SRBookListContract.IView view, SRMainModel model, String gradeId) {
        super(view, model);
        this.gradeId = gradeId;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getBooks(gradeId), new ZYNetSubscriber<ZYResponse<List<SRBook>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRBook>> response) {
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }
}
