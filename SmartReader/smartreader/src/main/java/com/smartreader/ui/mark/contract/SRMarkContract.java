package com.smartreader.ui.mark.contract;

import com.smartreader.base.mvp.ZYIBasePresenter;
import com.smartreader.base.mvp.ZYIBaseView;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;

import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public interface SRMarkContract {

    interface IView extends ZYIBaseView<IPresenter> {

    }

    interface IPresenter extends ZYIBasePresenter {

        String getMarkId(String tractId);

        SRPage getPage();

        String getBookId();

        List<SRTract> getTracks();
    }
}
