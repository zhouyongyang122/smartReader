package com.qudiandu.smartreader.ui.rank.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import rx.Observable;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRankModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRRank>>> getRanks(String school_id, String time_type, int start, int rows) {
        return mApi.getRanks(school_id, time_type, start, rows);
    }

    //supportType:1点赞 2:取消点赞
    public Observable<ZYResponse> support(String showId, int supportType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("show_id", showId);
        params.put("type", supportType + "");
        return mApi.support(params);
    }
}
