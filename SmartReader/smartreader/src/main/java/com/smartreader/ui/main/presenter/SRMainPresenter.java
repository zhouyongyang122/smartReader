package com.smartreader.ui.main.presenter;

import com.smartreader.SRApplication;
import com.smartreader.ZYPreferenceHelper;
import com.smartreader.base.bean.ZYResponse;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.main.model.SRMainModel;
import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.ui.main.contract.SRMainContract;
import com.smartreader.ui.main.model.bean.SRVersion;
import com.smartreader.utils.ZYSystemUtils;

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
}
