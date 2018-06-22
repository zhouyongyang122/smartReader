package com.qudiandu.smartdub.ui.myAudio.contract;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueNew;

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
