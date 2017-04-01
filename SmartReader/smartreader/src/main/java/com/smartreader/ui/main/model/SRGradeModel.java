package com.smartreader.ui.main.model;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBaseModel;
import com.smartreader.ui.main.model.bean.SRGrade;

import java.util.List;

import rx.Observable;

/**
 * Created by ZY on 17/3/31.
 */

public class SRGradeModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRGrade>>> getGrades() {
        return mApi.getGrades();
    }
}
