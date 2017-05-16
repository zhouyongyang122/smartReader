package com.qudiandu.smartreader.ui.main.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdert;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRVersion;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by ZY on 17/3/16.
 */

public class SRMainModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRAdert>>> getAdverts(String type) {
        return mApi.getAdverts(type);
    }

    public Observable<ZYResponse<List<SRBook>>> getBooks(String grade_id) {
        return mApi.getBooks(grade_id);
    }

    public Observable<ZYResponse<SRVersion>> getVersion() {
        return mApi.getVersion();
    }

    public Observable<ZYResponse<SRMarkResponse>> bookAddReport(String bookIds) {
        return mApi.bookAddReport(bookIds);
    }
}
