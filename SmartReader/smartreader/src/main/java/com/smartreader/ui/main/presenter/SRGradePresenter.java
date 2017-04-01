package com.smartreader.ui.main.presenter;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYListDataPresenter;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.main.contract.SRGradeContract;
import com.smartreader.ui.main.model.SRGradeModel;
import com.smartreader.ui.main.model.bean.SRGrade;

import java.util.List;

/**
 * Created by ZY on 17/3/31.
 */

public class SRGradePresenter extends ZYListDataPresenter<SRGradeContract.IView, SRGradeModel, SRGrade> implements SRGradeContract.IPresenter {

    public SRGradePresenter(SRGradeContract.IView view, SRGradeModel model) {
        super(view, model);
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getGrades(), new ZYNetSubscriber<ZYResponse<List<SRGrade>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRGrade>> response) {
                if (isRefresh()) {
                    mDataList.clear();
                }

                if (response.data != null && !response.data.isEmpty()) {
                    mDataList.addAll(response.data);
                }

                if (mDataList.isEmpty()) {
                    mView.showEmpty();
                } else {
                    mView.showList(false);
                }
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }
}
