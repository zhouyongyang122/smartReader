package com.smartreader.ui.main.model;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBaseModel;
import com.smartreader.ui.main.model.bean.SRAdert;

import java.util.List;

import rx.Observable;

/**
 * Created by ZY on 17/3/16.
 */

public class SRMainModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRAdert>>> getAdverts(String type) {
        return mApi.getAdverts(type);
    }
}
