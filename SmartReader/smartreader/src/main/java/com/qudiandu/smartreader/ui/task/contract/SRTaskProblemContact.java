package com.qudiandu.smartreader.ui.task.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;

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
