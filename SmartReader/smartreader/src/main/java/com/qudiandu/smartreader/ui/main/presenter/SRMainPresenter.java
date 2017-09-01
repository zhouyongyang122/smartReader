package com.qudiandu.smartreader.ui.main.presenter;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.ZYPreferenceHelper;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.ui.main.contract.SRMainContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRVersion;
import com.qudiandu.smartreader.ui.set.model.bean.SRMsgManager;
import com.qudiandu.smartreader.ui.set.model.bean.SRRemind;
import com.qudiandu.smartreader.utils.ZYSystemUtils;

/**
 * Created by ZY on 17/3/16.
 */

public class SRMainPresenter extends ZYBasePresenter implements SRMainContract.IPresenter {

    SRMainContract.IView mIView;

    SRMainModel mModel;

    long lastGetVersionTime;

    public SRMainPresenter(SRMainContract.IView iView) {
        mIView = iView;
        mModel = new SRMainModel();
        mIView.setPresenter(this);
    }

    public void getVersion() {
        if (lastGetVersionTime <= 0 && System.currentTimeMillis() - lastGetVersionTime > 10 * 60 * 1000) {
            lastGetVersionTime = System.currentTimeMillis();
            mSubscriptions.add(ZYNetSubscription.subscription(mModel.getVersion(), new ZYNetSubscriber<ZYResponse<SRVersion>>() {
                        @Override
                        public void onSuccess(ZYResponse<SRVersion> response) {
                            super.onSuccess(response);
                            if (response.data == null) {
                                lastGetVersionTime = 0;
                            } else {
                                SRVersion version = response.data;
                                long timeInMillis = System.currentTimeMillis() / 1000L;
                                long timeRange = version.timestamp - timeInMillis;
                                ZYPreferenceHelper.getInstance().setTimeOffset(timeRange);
                                if (version.versioncode > ZYSystemUtils.getAppVersionCode(SRApplication.getInstance().getCurrentActivity())) {
                                    mIView.showUpdateView(version);
                                }
                            }
                        }

                        @Override
                        public void onFail(String message) {
                            super.onFail(message);
                            lastGetVersionTime = 0;
                        }
                    }

            ));
        }
    }

    public void msgRemind() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.msgRemind(), new ZYNetSubscriber<ZYResponse<SRRemind>>() {
            @Override
            public void onSuccess(ZYResponse<SRRemind> response) {
                super.onSuccess(response);
                if (response.data != null) {
                    int first_msgid = response.data.first_msgid;
                    SRMsgManager.getInstance().saveMsgRemind(first_msgid);
                }
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }
}
