package com.qudiandu.smartdub.base.mvp;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.html5.ZYHtml5UrlBean;
import com.qudiandu.smartdub.service.net.ZYNetManager;
import com.qudiandu.smartdub.service.net.ZYRequestApi;

import rx.Observable;

/**
 * Created by ZY on 17/3/14.
 */

public class ZYBaseModel {

    protected ZYRequestApi mApi;

    public ZYBaseModel() {
        mApi = ZYNetManager.shareInstance().getApi();
    }

    public Observable<ZYResponse<ZYHtml5UrlBean>> getHtml5Urls() {
        return mApi.getHtml5Urls();
    }
}
