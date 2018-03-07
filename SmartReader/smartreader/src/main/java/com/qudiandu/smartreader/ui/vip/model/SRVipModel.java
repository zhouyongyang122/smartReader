package com.qudiandu.smartreader.ui.vip.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.vip.model.bean.SRVip;
import com.qudiandu.smartreader.ui.vip.model.bean.SRVipOrder;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 18/3/1.
 */

public class SRVipModel extends ZYBaseModel {

    public Observable<ZYResponse<SRVip>> getVipPackages() {
        return mApi.getVipPackages();
    }

    public Observable<ZYResponse<SRVipOrder>> getVipPayOrder(String amount, int type, String pid, String days) {
        Map<String, String> params = new HashMap<>();
        params.put("amount", amount + "");
        params.put("type", type + "");
        params.put("pid", pid);
        params.put("days", days + "");
        return mApi.getVipPayOrder(params);
    }
}
