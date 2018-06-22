package com.qudiandu.smartdub.ui.task.contract;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.main.model.bean.SRTask;
import com.qudiandu.smartdub.ui.task.model.bean.SRTaskFinish;

/**
 * Created by ZY on 17/7/24.
 */

public interface SRTaskDetailContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void refreshHeader(SRTask task);
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRTaskFinish> {
        boolean isProblemTask();
        SRTask getTask();
    }
}
