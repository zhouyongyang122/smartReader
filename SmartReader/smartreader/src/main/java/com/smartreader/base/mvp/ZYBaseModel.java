package com.smartreader.base.mvp;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.html5.ZYHtml5UrlBean;
import com.smartreader.service.net.ZYNetManager;
import com.smartreader.service.net.ZYRequestApi;

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
