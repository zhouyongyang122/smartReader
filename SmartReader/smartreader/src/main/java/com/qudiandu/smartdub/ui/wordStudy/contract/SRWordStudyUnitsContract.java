package com.qudiandu.smartdub.ui.wordStudy.contract;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;
import com.qudiandu.smartdub.ui.wordStudy.model.bean.SRWordStudyUnit;

/**
 * Created by ZY on 17/12/11.
 */

public interface SRWordStudyUnitsContract {

    interface IView extends ZYListDataContract.View<IPresenter> {

    }

    interface IPresenter extends ZYListDataContract.Presenter<SRWordStudyUnit> {
        SRBook getBook();
    }
}
