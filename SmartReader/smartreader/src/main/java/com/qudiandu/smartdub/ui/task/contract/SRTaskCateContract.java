package com.qudiandu.smartdub.ui.task.contract;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.task.model.bean.SRTaskCate;

/**
 * Created by ZY on 17/7/23.
 */

public interface SRTaskCateContract {

    interface IView extends ZYListDataContract.View<IPresenter> {

    }

    interface IPresenter extends ZYListDataContract.Presenter<Object> {
        void addTask();
    }
}
