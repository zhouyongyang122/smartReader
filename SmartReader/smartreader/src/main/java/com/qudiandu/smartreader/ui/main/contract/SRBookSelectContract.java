package com.qudiandu.smartreader.ui.main.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;

/**
 * Created by ZY on 17/3/31.
 */

public interface SRBookSelectContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRBook> {
        void reportAddBookts();

        String getGradeName();

        boolean isTaskSel();
    }
}
