package com.smartreader.ui.profile.model;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBaseModel;
import com.smartreader.ui.login.model.bean.SRUser;

import java.util.Map;

import retrofit2.http.Body;
import rx.Observable;

/**
 * Created by ZY on 17/4/7.
 */

public class SREditModel extends ZYBaseModel {

    public Observable<ZYResponse<SRUser>> editUser(Map<String, String> params) {
        return mApi.editUser(params);
    }
}
