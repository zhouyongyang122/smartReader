package com.qudiandu.smartdub.thirdParty.pay;

/**
 * Created by ZY on 18/3/1.
 */

public interface SRPayResultCallback {

    int CODE_SUCCESS = 1;
    int CODE_CANCEL = 2;
    int CODE_FAIL = 0;

    void onPayResult(int code, String msg);
}
