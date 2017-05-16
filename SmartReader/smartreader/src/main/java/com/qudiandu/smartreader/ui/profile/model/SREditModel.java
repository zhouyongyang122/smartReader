package com.qudiandu.smartreader.ui.profile.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;

import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 17/4/7.
 */

public class SREditModel extends ZYBaseModel {

    public Observable<ZYResponse<SRUser>> editUser(Map<String, String> params) {
        return mApi.editUser(params);
    }
}
