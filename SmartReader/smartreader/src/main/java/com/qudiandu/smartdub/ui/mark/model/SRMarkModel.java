package com.qudiandu.smartdub.ui.mark.model;

import android.text.TextUtils;

import com.qudiandu.smartdub.ZYPreferenceHelper;
import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYBaseModel;
import com.qudiandu.smartdub.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartdub.ui.dubbing.model.bean.SRMarkResponse;

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

    public Observable<ZYResponse<SRCatalogueResponse>> catalogueAdd(String audio, String book_id, String catalogue_id, int score, String audio_track, String group_id, String task_id) {
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("audio", audio);
        paramas.put("book_id", book_id.equals("0") ? "1" : book_id);
        paramas.put("catalogue_id", catalogue_id);
        paramas.put("score", score + "");
        paramas.put("audio_track", audio_track);
        if (!TextUtils.isEmpty(group_id)) {
            paramas.put("group_id", group_id);
            paramas.put("task_id", task_id);
        } else if (!TextUtils.isEmpty(ZYPreferenceHelper.getInstance().getClassId())) {
            paramas.put("group_id", ZYPreferenceHelper.getInstance().getClassId());
        }
        return mApi.catalogueAdd(paramas);
    }
}
