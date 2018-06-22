package com.qudiandu.smartdub.ui.main.contract;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.login.model.bean.SRUser;
import com.qudiandu.smartdub.ui.main.model.bean.SRClass;

/**
 * Created by ZY on 17/7/24.
 */

public interface SRClassUsersContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void delUserSuc();
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRUser> {
        void removeUsers();
    }
}
