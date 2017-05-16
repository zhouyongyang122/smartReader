package com.qudiandu.smartreader.ui.set.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.set.model.bean.SRSysMsg;

import java.util.List;

import rx.Observable;

/**
 * Created by ZY on 17/4/9.
 */

public class SRSetModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRSysMsg>>> getSysMsgs(int start, int rows) {
        return mApi.getSysMsgs(start,rows);
    }
}
