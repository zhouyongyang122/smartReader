package com.smartreader.base.mvp;

import com.smartreader.service.net.ZYNetManager;
import com.smartreader.service.net.ZYRequestApi;

/**
 * Created by ZY on 17/3/14.
 */

public class ZYBaseModel {

    protected ZYRequestApi mApi;

    public ZYBaseModel(){
        mApi = ZYNetManager.shareInstance().getApi();
    }
}
