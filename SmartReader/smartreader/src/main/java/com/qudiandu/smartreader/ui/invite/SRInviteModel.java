package com.qudiandu.smartreader.ui.invite;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 上午10:56$
 */
public class SRInviteModel extends ZYBaseModel {

    public Observable<ZYResponse<SRInCome>> getInCome() {
        return mApi.getInCome();
    }

    public Observable<ZYResponse<List<SRInviteInfo>>> getInvites(int start, int rows) {
        return mApi.getInvites(start, rows);
    }

    public Observable<ZYResponse> withdraw(String amount) {
        Map<String, String> params = new HashMap<>();
        params.put("amount", amount);
        return mApi.withdraw(params);
    }

    public Observable<ZYResponse<SRUser>> bindThird(Map<String, String> params) {
        return mApi.bindThird(params);
    }
}
