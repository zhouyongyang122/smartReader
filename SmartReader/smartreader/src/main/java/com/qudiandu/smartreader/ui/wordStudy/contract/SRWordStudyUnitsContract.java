package com.qudiandu.smartreader.ui.wordStudy.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyUnit;

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
