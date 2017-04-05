package com.smartreader.ui.main.contract;

import com.smartreader.base.mvp.ZYIBasePresenter;
import com.smartreader.base.mvp.ZYIBaseView;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.ui.main.model.bean.SRVersion;

import java.util.List;

/**
 * Created by ZY on 17/3/16.
 */

public interface SRMainContract {

    interface IView extends ZYIBaseView<IPresenter> {

        void showUpdateView(SRVersion version);

    }

    interface IPresenter extends ZYIBasePresenter {
        void getVersion();
    }
}
