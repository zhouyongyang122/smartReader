package com.smartreader.ui.main.contract;

import com.smartreader.base.mvp.ZYListDataContract;
import com.smartreader.ui.main.model.bean.SRGrade;

/**
 * Created by ZY on 17/3/31.
 */

public interface SRGradeContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRGrade> {

    }
}
