package com.smartreader.ui.mark.model;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBaseModel;
import com.smartreader.ui.main.model.bean.SRAdert;
import com.smartreader.ui.mark.model.bean.SRMarkResponse;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkModel extends ZYBaseModel {

    public Observable<ZYResponse<SRMarkResponse>> trackAdd(Map<String, String> params) {
        return mApi.trackAdd(params);
    }
}
