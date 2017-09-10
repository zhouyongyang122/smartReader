package com.qudiandu.smartreader.ui.task.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;

/**
 * Created by ZY on 17/7/24.
 */

public interface SRTaskDetailContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void refreshHeader(SRTask task);
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRTaskFinish> {
        boolean isProblemTask();
    }
}
