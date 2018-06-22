package com.qudiandu.smartdub.ui.main.model;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYBaseModel;
import com.qudiandu.smartdub.ui.main.model.bean.SRGrade;

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
