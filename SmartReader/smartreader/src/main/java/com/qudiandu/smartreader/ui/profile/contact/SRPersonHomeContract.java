package com.qudiandu.smartreader.ui.profile.contact;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;

/**
 * Created by ZY on 18/3/7.
 */

public interface SRPersonHomeContract {

    interface IView extends ZYListDataContract.View<IPresenter> {

    }

    interface IPresenter extends ZYListDataContract.Presenter<SRCatalogueNew> {
        SRUser getUser();
    }
}
