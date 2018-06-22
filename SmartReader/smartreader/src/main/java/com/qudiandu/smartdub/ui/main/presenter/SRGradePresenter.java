package com.qudiandu.smartdub.ui.main.presenter;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.main.contract.SRGradeContract;
import com.qudiandu.smartdub.ui.main.model.SRGradeModel;
import com.qudiandu.smartdub.ui.main.model.bean.SRGrade;

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
