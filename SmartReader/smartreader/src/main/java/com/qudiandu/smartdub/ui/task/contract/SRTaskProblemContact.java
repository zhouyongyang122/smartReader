package com.qudiandu.smartdub.ui.task.contract;

import com.qudiandu.smartdub.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartdub.base.mvp.ZYIBaseView;
import com.qudiandu.smartdub.ui.task.model.bean.SRTaskProblem;

/**
 * Created by ZY on 17/9/3.
 */

public interface SRTaskProblemContact {

    interface IView extends ZYIBaseView<IPresenter> {

    }

    interface IPresenter extends ZYIBasePresenter {
        boolean isFinised();

        SRTaskProblem.Problem getProblem();

        SRTaskProblem.Teacher getTeacher();

        void setFinised(boolean isFinised);
    }
}
