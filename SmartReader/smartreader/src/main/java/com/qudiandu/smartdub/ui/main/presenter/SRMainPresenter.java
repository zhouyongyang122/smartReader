package com.qudiandu.smartdub.ui.main.presenter;

import com.qudiandu.smartdub.SRApplication;
import com.qudiandu.smartdub.ZYPreferenceHelper;
import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.service.net.ZYNetManager;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.main.model.SRMainModel;
import com.qudiandu.smartdub.base.mvp.ZYBasePresenter;
import com.qudiandu.smartdub.ui.main.contract.SRMainContract;
import com.qudiandu.smartdub.ui.main.model.bean.SRVersion;
import com.qudiandu.smartdub.ui.set.model.bean.SRMsgManager;
import com.qudiandu.smartdub.ui.set.model.bean.SRRemind;
import com.qudiandu.smartdub.utils.ZYLog;
import com.qudiandu.smartdub.utils.ZYSystemUtils;

import cn.jpush.android.api.JPushInterface;

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

    public void uploadJpushId() {
        //jpush-id:190e35f7e0414ba9a8b
        try {
            ZYLog.e(getClass().getSimpleName(), "Jpush-id:" + JPushInterface.getRegistrationID(SRApplication.getInstance().getCurrentActivity()));
            if (!ZYPreferenceHelper.getInstance().hasUploadJPushId()) {
                mSubscriptions.add(ZYNetSubscription.subscription(ZYNetManager.shareInstance().getApi().pushInfo(JPushInterface.getRegistrationID(SRApplication.getInstance().getCurrentActivity())), new ZYNetSubscriber() {
                    @Override
                    public void onSuccess(ZYResponse response) {
                        ZYLog.e(getClass().getSimpleName(), "Jpush-id上传成功...............");
                        ZYPreferenceHelper.getInstance().setUploadJPushId(true);
                    }

                    @Override
                    public void onFail(String message) {

                    }
                }));
            }
        } catch (Exception e) {

        }
    }
}
