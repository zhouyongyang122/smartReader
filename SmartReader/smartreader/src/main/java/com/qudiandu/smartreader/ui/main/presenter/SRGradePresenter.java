package com.qudiandu.smartreader.ui.main.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.contract.SRGradeContract;
import com.qudiandu.smartreader.ui.main.model.SRGradeModel;
import com.qudiandu.smartreader.ui.main.model.bean.SRGrade;

import java.util.List;

/**
 * Created by ZY on 17/3/31.
 */

public class SRGradePresenter extends ZYListDataPresenter<SRGradeContract.IView, SRGradeModel, SRGrade> implements SRGradeContract.IPresenter {

    boolean isTaskSel;

    public SRGradePresenter(SRGradeContract.IView view, boolean isTaskSel) {
        super(view, new SRGradeModel());
        this.isTaskSel = isTaskSel;
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

    public boolean isTaskSel() {
        return isTaskSel;
    }
}
