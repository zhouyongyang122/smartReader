package com.qudiandu.smartreader.ui.main.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;

/**
 * Created by ZY on 17/7/24.
 */

public interface SRClassDetailContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void refreshHeader(SRClass data);
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRUser> {
        SRClass getClassDetail();
    }
}
