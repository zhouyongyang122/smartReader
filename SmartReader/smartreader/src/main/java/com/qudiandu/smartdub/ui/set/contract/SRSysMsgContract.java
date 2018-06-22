package com.qudiandu.smartdub.ui.set.contract;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.set.model.bean.SRSysMsg;

/**
 * Created by ZY on 17/4/9.
 */

public interface SRSysMsgContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRSysMsg> {

    }
}
