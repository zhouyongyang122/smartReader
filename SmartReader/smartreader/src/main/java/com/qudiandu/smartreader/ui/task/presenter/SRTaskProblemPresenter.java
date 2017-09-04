package com.qudiandu.smartreader.ui.task.presenter;

import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.ui.task.contract.SRTaskProblemContact;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;

/**
 * Created by ZY on 17/9/3.
 */

public class SRTaskProblemPresenter extends ZYBasePresenter implements SRTaskProblemContact.IPresenter {

    SRTaskProblem.Problem mProblem;

    SRTaskProblemContact.IView mView;

    SRTaskProblem.Teacher mTeacher;

    boolean mIsFinised;

    public SRTaskProblemPresenter(SRTaskProblemContact.IView iView, SRTaskProblem.Problem problem, SRTaskProblem.Teacher teacher) {
        mView = iView;
        mProblem = problem;
        mView.setPresenter(this);
        mTeacher = teacher;
    }

    public boolean isFinised() {
        return mIsFinised;
    }

    public SRTaskProblem.Problem getProblem() {
        return mProblem;
    }

    public void setFinised(boolean isFinised) {
        mIsFinised = isFinised;
    }

    public SRTaskProblem.Teacher getTeacher() {
        return mTeacher;
    }
}
