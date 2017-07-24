package com.qudiandu.smartreader.ui.task.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskCate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 17/7/23.
 */

public class SRTaskCateModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRTaskCate>>> getTaskCates(String book_id, int start, int rows) {
        return mApi.getTaskCates(book_id, start, rows);
    }

    public Observable<ZYResponse> addTask(String group_id, String book_id, String catalogue_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group_id", group_id);
        params.put("book_id", book_id);
        params.put("catalogue_id", catalogue_id);
        return mApi.addTask(params);
    }
}
