package com.qudiandu.smartreader.ui.task.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskCate;

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
