package com.smartreader.ui.profile.contact;

import com.smartreader.base.mvp.ZYIBasePresenter;
import com.smartreader.base.mvp.ZYIBaseView;

import java.util.Map;

/**
 * Created by ZY on 17/4/7.
 */

public interface SREditContract {
    interface IView extends ZYIBaseView<IPresenter> {

    }

    interface IPresenter extends ZYIBasePresenter {
        void editUser(Map<String, String> params);
    }
}
