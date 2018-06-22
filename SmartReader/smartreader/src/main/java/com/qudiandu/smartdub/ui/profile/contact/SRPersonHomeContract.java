package com.qudiandu.smartdub.ui.profile.contact;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.login.model.bean.SRUser;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueNew;

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
