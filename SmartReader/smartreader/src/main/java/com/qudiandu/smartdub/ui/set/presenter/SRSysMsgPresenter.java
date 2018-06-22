package com.qudiandu.smartdub.ui.set.presenter;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.set.contract.SRSysMsgContract;
import com.qudiandu.smartdub.ui.set.model.SRSetModel;
import com.qudiandu.smartdub.ui.set.model.bean.SRSysMsg;

import java.util.List;

/**
 * Created by ZY on 17/4/9.
 */

public class SRSysMsgPresenter extends ZYListDataPresenter<SRSysMsgContract.IView, SRSetModel, SRSysMsg> implements SRSysMsgContract.IPresenter {

    public SRSysMsgPresenter(SRSysMsgContract.IView view, SRSetModel model) {
        super(view, model);
        mRows = 100;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getSysMsgs(mStart, mRows), new ZYNetSubscriber<ZYResponse<List<SRSysMsg>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRSysMsg>> response) {
//               super.onSuccess(response);
                success(response);
            }

            @Override
            public void onFail(String message) {
//               super.onFail(message);
                fail(message);
            }
        }));
    }


}
