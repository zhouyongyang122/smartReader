package com.qudiandu.smartreader.ui.dubbing.model;

import android.text.TextUtils;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkResponse;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 17/12/1.
 */

public class SRDubbingModel extends ZYBaseModel {

    public Observable<ZYResponse<SRMarkResponse>> trackAdd(Map<String, String> params) {
        return mApi.trackAdd(params);
    }

    public Observable<ZYResponse<SRCatalogueResponse>> catalogueAdd(String audio,
                                                                    String book_id,
                                                                    String catalogue_id,
                                                                    int score,
                                                                    String audio_track,
                                                                    String group_id,
                                                                    String task_id,
                                                                    String other_score) {
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("audio", audio);
        paramas.put("book_id", book_id.equals("0") ? "1" : book_id);
        paramas.put("catalogue_id", catalogue_id);
        paramas.put("score", score + "");
        paramas.put("audio_track", audio_track);
        if (!TextUtils.isEmpty(group_id)) {
            paramas.put("group_id", group_id);
            paramas.put("task_id", task_id);
        }

        if(!TextUtils.isEmpty(other_score)){
            paramas.put("other_score",other_score);
        }
        return mApi.catalogueAdd(paramas);
    }
}
