package com.qudiandu.smartreader.ui.main.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.main.model.bean.SRVersion;

/**
 * Created by ZY on 17/3/16.
 */

public interface SRMainContract {

    interface IView extends ZYIBaseView<IPresenter> {

        void showUpdateView(SRVersion version);

    }

    interface IPresenter extends ZYIBasePresenter {
        void getVersion();
        void msgRemind();
        void uploadJpushId();
    }
}
