package com.qudiandu.smartdub.ui.wordStudy.model;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYBaseModel;
import com.qudiandu.smartdub.ui.wordStudy.model.bean.SRWordStudyUnit;
import com.qudiandu.smartdub.ui.wordStudy.model.bean.SRWordStudyWord;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRWordStudyUnit>>> getBookUnits(String book_id) {
        return mApi.getBookUnits(book_id);
    }

    public Observable<ZYResponse<List<SRWordStudyWord>>> getUnitWords(String unit_id) {
        return mApi.getUnitWords(unit_id);
    }

}
