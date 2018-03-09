package com.qudiandu.smartreader.thirdParty.pay;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.utils.ZYToast;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import static com.qudiandu.smartreader.ui.SRAppConstants.WECHAT_APP_KEY;

/**
 * Created by ZY on 18/3/1.
 */

public class SRWxPay {

    public static final String APP_ID = WECHAT_APP_KEY;

    public interface SRWXPayCallBack {
        /**
         * @param result: 1支付成功; 2失败; 3取消
         * @return void
         * @function onResult
         * @Description
         * @date 2015年7月24日下午4:31:37
         */
        void onWXPayResult(int result, String msg);
    }

    private IWXAPI api;

    public SRWxPay() {
        api = WXAPIFactory.createWXAPI(SRApplication.getInstance(), APP_ID);
    }

    public void sendPayReq(String wx_app_id, String wx_mch_account, String prepay_id, String nonce_str, String sign, String timestamp) {
        if (!api.isWXAppInstalled()) {
            ZYToast.show(SRApplication.getInstance(), "没有安装微信!");
        } else if (!api.isWXAppSupportAPI()) {
            ZYToast.show(SRApplication.getInstance(), "微信当前版本不支持支付!");
        } else {
            PayReq req = new PayReq();
            req.appId = wx_app_id;
            req.partnerId = wx_mch_account;
            req.prepayId = prepay_id;
            req.nonceStr = nonce_str;
            req.packageValue = "Sign=WXPay";
            req.sign = sign;
            req.timeStamp = timestamp;
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信�
            api.sendReq(req);
        }

    }
}
