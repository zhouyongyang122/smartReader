package com.qudiandu.smartreader.ui.mark.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.mark.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkResponse;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkModel extends ZYBaseModel {

    public Observable<ZYResponse<SRMarkResponse>> trackAdd(Map<String, String> params) {
        return mApi.trackAdd(params);
    }

    public Observable<ZYResponse<SRCatalogueResponse>> catalogueAdd(String audio, String book_id, String catalogue_id, String audio_track) {
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("audio", audio);
        paramas.put("book_id", book_id.equals("0") ? "1" : book_id);
        paramas.put("catalogue_id", catalogue_id);
        paramas.put("audio_track", audio_track);
        return mApi.catalogueAdd(paramas);
    }
}
