package com.qudiandu.smartreader.ui.myAudio.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;

/**
 * Created by ZY on 17/6/10.
 */

public interface SRCataloguesContract {

    interface IView extends ZYListDataContract.View<IPresenter> {

        void delCatalogueSuc(SRCatalogueNew data);
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRCatalogueNew> {
        void setEdit(boolean isEditing);

        void delCatalogues(final SRCatalogueNew data);

        SRCatalogueNew getDelData();

        void changeSelectedValue(int position);
    }
}
