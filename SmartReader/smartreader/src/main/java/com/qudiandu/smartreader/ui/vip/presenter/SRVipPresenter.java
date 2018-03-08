package com.qudiandu.smartreader.ui.vip.presenter;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.pay.SRAliPay;
import com.qudiandu.smartreader.thirdParty.pay.SRWxPay;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.vip.contract.SRVipContract;
import com.qudiandu.smartreader.ui.vip.model.SRVipModel;
import com.qudiandu.smartreader.ui.vip.model.bean.SRVip;
import com.qudiandu.smartreader.ui.vip.model.bean.SRVipOrder;
import com.qudiandu.smartreader.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 18/3/6.
 */

public class SRVipPresenter extends ZYBasePresenter implements SRVipContract.IPresenter, SRAliPay.SRAliPayCallBack, SRWxPay.SRWXPayCallBack {

    public static int PAY_WECHAT_TYPE = 3;

    public static int PAY_ALIPAY_TYPE = 1;

    SRVipContract.IView mView;

    SRVipModel mModel;

    SRVip mVip;

    List<SRVip.Rights> mRightsList = new ArrayList<SRVip.Rights>();

    List<SRVip.Price> mPriceList = new ArrayList<SRVip.Price>();

    public SRVipPresenter(SRVipContract.IView iView) {
        mView = iView;
        mView.setPresenter(this);
        mModel = new SRVipModel();
        addRights();
    }

    private void addRights() {
        SRVip.Rights rights = new SRVip.Rights();
        rights.res = R.drawable.icon_englishtest;
        rights.title = "专业语音评测";
        mRightsList.add(rights);

        rights = new SRVip.Rights();
        rights.res = R.drawable.icon_vocabularypractice;
        rights.title = "单元词汇练习";
        mRightsList.add(rights);

        rights = new SRVip.Rights();
        rights.res = R.drawable.icon_textbookreading;
        rights.title = "所有教材点读";
        mRightsList.add(rights);

        rights = new SRVip.Rights();
        rights.res = R.drawable.icon_jobmanagement;
        rights.title = "班级作业管理";
        mRightsList.add(rights);

        rights = new SRVip.Rights();
        rights.res = R.drawable.icon_authoritativedictionary;
        rights.title = "专业权威词典";
        mRightsList.add(rights);

        rights = new SRVip.Rights();
        rights.res = R.drawable.icon_personalitydisplay;
        rights.title = "作品个性展示";
        mRightsList.add(rights);
    }

    @Override
    public void loadVip() {

        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getVipPackages(), new ZYNetSubscriber<ZYResponse<SRVip>>() {

            @Override
            public void onSuccess(ZYResponse<SRVip> response) {
                if (response != null && response.data != null) {
                    mVip = response.data;
                    SRUser user = SRUserManager.getInstance().getUser();
                    user.is_vip = mVip.is_vip;
                    user.vip_endtime = mVip.vip_endtime;
                    SRUserManager.getInstance().setUser(user);
                    mPriceList.addAll(mVip.package_list);
                    mPriceList.get(0).isSelected = true;
                    mView.showList(false);
                } else {
                    mView.showError();
                }
            }

            @Override
            public void onFail(String message) {
                mView.showError();
            }
        }));
    }

    @Override
    public void buy(SRVip.Price price, final int payType) {

        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getVipPayOrder(price.amount, payType, price.id + "", price.days + ""), new ZYNetSubscriber<ZYResponse<SRVipOrder>>() {

            @Override
            public void onSuccess(ZYResponse<SRVipOrder> response) {
                if (response != null && response.data != null) {
                    SRVipOrder strateBean = response.data;
                    if (payType == PAY_ALIPAY_TYPE) {
                        new SRAliPay().sendPayReq(SRApplication.getInstance().getCurrentActivity(), SRVipPresenter.this, strateBean.alipay_private_key, strateBean.alipay_pid, strateBean.alipay_account, strateBean.order_id, strateBean.title, strateBean.desc, strateBean.amount, strateBean.return_url);
                    } else if (payType == PAY_WECHAT_TYPE) {
                        WXPayEntryActivity.payCallBack = SRVipPresenter.this;
                        new SRWxPay().sendPayReq(strateBean.wx_app_id, strateBean.wx_mch_account, strateBean.prepay_id, strateBean.nonce_str, strateBean.sign, strateBean.timestamp);
                    }
                } else {
                    onFail("购买失败,请重新尝试!");
                }
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                mView.buyFail();
            }
        }));
    }

    public SRVip getVip() {
        return mVip;
    }

    public List<SRVip.Rights> getRightsList() {
        return mRightsList;
    }

    @Override
    public void onWXPayResult(int result, String msg) {
        WXPayEntryActivity.payCallBack = null;
        mView.showToast(msg);
        if (result == 1) {
            mView.buySuccess();
        } else if (result == 2) {
            //失败
            mView.buyFail();
        } else {
            //取消
            mView.buyFail();
        }
    }

    @Override
    public void onAliPayResult(int result, String msg) {
        mView.showToast(msg);
        if (result == 1) {
            mView.buySuccess();
        } else if (result == 3) {
            mView.buyFail();
        }
    }

    public List<SRVip.Price> getPriceList() {
        return mPriceList;
    }
}
