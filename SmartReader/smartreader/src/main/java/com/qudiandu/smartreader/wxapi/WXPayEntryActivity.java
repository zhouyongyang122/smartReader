package com.qudiandu.smartreader.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.qudiandu.smartreader.thirdParty.pay.SRWxPay;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYStatusBarUtils;
import com.qudiandu.smartreader.utils.ZYToast;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by ZY on 18/3/1.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    public static SRWxPay.SRWXPayCallBack payCallBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, SRWxPay.APP_ID);
        api.handleIntent(getIntent(), this);

        ZYStatusBarUtils.tintStatusBar(this, Color.TRANSPARENT, 0);
    }

    @Override
    protected void onDestroy() {
        payCallBack = null;
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        ZYLog.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        switch (resp.errCode) {
            //正确返回
            case BaseResp.ErrCode.ERR_OK:
                if (payCallBack != null) {
                    payCallBack.onWXPayResult(1, "支付成功");
                } else {
                    ZYToast.show(this, "支付成功");
                }
                break;
            //用户取消
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (payCallBack != null) {
                    payCallBack.onWXPayResult(3, "支付取消");
                } else {
                    ZYToast.show(this, "支付取消");
                }
                break;
            //一般错误
            case BaseResp.ErrCode.ERR_COMM:
            default:
                if (payCallBack != null) {
                    payCallBack.onWXPayResult(2, "支付失败");
                } else {
                    ZYToast.show(this, "支付失败");
                }
                break;
        }
        finish();
    }
}
