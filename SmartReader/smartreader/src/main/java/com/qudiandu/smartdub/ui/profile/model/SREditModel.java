package com.qudiandu.smartdub.ui.profile.model;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYBaseModel;
import com.qudiandu.smartdub.ui.login.model.bean.SRUser;

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
