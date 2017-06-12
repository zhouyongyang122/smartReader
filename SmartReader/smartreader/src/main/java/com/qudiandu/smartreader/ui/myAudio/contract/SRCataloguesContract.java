package com.qudiandu.smartreader.ui.myAudio.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;

/**
 * Created by ZY on 17/6/10.
 */

public interface SRCataloguesContract {

    interface IView extends ZYListDataContract.View<IPresenter> {

    }

    interface IPresenter extends ZYListDataContract.Presenter<SRCatalogueNew> {

    }
}
