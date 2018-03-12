package com.qudiandu.smartreader.ui.login.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;

import java.util.Map;

/**
 * Created by ZY on 17/4/4.
 */

public interface SRLoginContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void loginSuccess(String msg);
    }

    interface IPresenter extends ZYIBasePresenter {
        void login(String mobile, String pwd);
        void loginByThrid(Map<String,String> paramas);
        boolean isNeedGoVip();
    }
}
