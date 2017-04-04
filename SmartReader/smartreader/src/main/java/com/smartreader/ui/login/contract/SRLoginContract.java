package com.smartreader.ui.login.contract;

import com.smartreader.base.mvp.ZYIBasePresenter;
import com.smartreader.base.mvp.ZYIBaseView;

/**
 * Created by ZY on 17/4/4.
 */

public interface SRLoginContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void loginSuccess(String msg);
    }

    interface IPresenter extends ZYIBasePresenter {
        void login(String mobile, String pwd);
    }
}
