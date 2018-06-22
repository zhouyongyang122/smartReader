package com.qudiandu.smartdub.ui.main.contract;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.main.model.bean.SRGrade;

/**
 * Created by ZY on 17/3/31.
 */

public interface SRGradeContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<SRGrade> {
        boolean isTaskSel();
    }
}
