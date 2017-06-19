package com.qudiandu.smartreader.ui.myAudio.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 17/6/10.
 */

public class SRMyAudioModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRCatalogueNew>>> getCatalogues(int start, int rows) {
        return mApi.getCatalogues(start, rows);
    }

    public Observable<ZYResponse<SRCatalogueDetail>> getCatalogueDetail(String show_id) {
        return mApi.getCatalogueDetail(show_id);
    }

    public Observable<ZYResponse> delCatalogue(String show_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("show_id", show_id);
        return mApi.delCatalogue(params);
    }
}
