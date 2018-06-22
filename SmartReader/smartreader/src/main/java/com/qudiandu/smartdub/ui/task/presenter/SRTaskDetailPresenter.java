package com.qudiandu.smartdub.ui.task.presenter;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.main.model.bean.SRTask;
import com.qudiandu.smartdub.ui.task.contract.SRTaskDetailContract;
import com.qudiandu.smartdub.ui.task.model.SRTaskModel;
import com.qudiandu.smartdub.ui.task.model.bean.SRTaskFinish;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskDetailPresenter extends ZYListDataPresenter<SRTaskDetailContract.IView, SRTaskModel, SRTaskFinish> implements SRTaskDetailContract.IPresenter {

    SRTask mTask;

    boolean mIsProblemTask;

    public SRTaskDetailPresenter(SRTaskDetailContract.IView view, SRTask task) {
        super(view, new SRTaskModel());
        mTask = task;
        mRows = 100;
        mIsProblemTask = mTask.ctype != SRTask.TASK_TYPE_RECORD;
    }

    @Override
    protected void loadData() {
        Observable<ZYResponse<List<SRTaskFinish>>> observable = null;
        if (mIsProblemTask) {
            observable = mModel.getProblemFinishs(mTask.group_id + "", mTask.task_id + "", mStart, mRows);
        } else {
            observable = mModel.getTaskFinishs(mTask.group_id + "", mTask.task_id + "", mStart, mRows);
        }
        mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<List<SRTaskFinish>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRTaskFinish>> response) {
                if (isRefresh()) {
                    mView.refreshHeader(mTask);
                    if (response.data == null || response.data.size() <= 0) {
                        ArrayList<SRTaskFinish> data = new ArrayList<SRTaskFinish>();
                        data.add(new SRTaskFinish());
                        response.data = data;
                    }
                }
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    public boolean isProblemTask() {
        return mIsProblemTask;
    }

    public SRTask getTask() {
        return mTask;
    }
}