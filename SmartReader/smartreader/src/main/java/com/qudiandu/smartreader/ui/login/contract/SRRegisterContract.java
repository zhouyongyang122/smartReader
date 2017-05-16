package com.qudiandu.smartreader.ui.login.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;

/**
 * Created by ZY on 17/4/4.
 */

public interface SRRegisterContract {

    interface IView extends ZYIBaseView<IPresenter> {

        void codeSuccess(String msg);

        void registerSuccess(String msg);

        void registerError(String error);

        void codeError(String error);
    }

    interface IPresenter extends ZYIBasePresenter {

        int getType();

        void register(String mobile, String code, String pwd);

        void changePwd(String oldPwd, String newPwd);

        void getCode(String mobile);
    }
}
