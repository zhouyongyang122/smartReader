package com.qudiandu.smartreader.base.mvp;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.html5.ZYHtml5UrlBean;
import com.qudiandu.smartreader.service.net.ZYNetManager;
import com.qudiandu.smartreader.service.net.ZYRequestApi;

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
