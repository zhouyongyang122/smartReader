package com.smartreader.ui.set.presenter;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.base.mvp.ZYListDataContract;
import com.smartreader.base.mvp.ZYListDataPresenter;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.set.contract.SRSysMsgContract;
import com.smartreader.ui.set.model.SRSetModel;
import com.smartreader.ui.set.model.bean.SRSysMsg;

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
