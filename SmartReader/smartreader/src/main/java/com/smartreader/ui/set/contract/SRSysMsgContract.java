package com.smartreader.ui.set.contract;

import com.smartreader.base.mvp.ZYListDataContract;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.set.model.bean.SRSysMsg;

/**
 * Created by ZY on 17/4/9.
 */

public interface SRSysMsgContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRSysMsg> {

    }
}
