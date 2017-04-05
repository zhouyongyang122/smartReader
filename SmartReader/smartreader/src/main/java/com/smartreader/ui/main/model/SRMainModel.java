package com.smartreader.ui.main.model;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBaseModel;
import com.smartreader.ui.main.model.bean.SRAdert;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.main.model.bean.SRVersion;

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
}
