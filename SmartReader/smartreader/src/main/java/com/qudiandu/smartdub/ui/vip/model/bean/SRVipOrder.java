package com.qudiandu.smartdub.ui.vip.model.bean;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 18/3/6.
 */

public class SRVipOrder implements ZYIBaseBean {

    public String order_id;

    public String title;

    public String desc;

    public String amount;

    public String return_url;

    public String type;

    public String alipay_pid;

    public String alipay_account;

    public String alipay_public_key;

    public String alipay_private_key;

    public String wx_app_id;

    public String wx_mch_account;

    public String nonce_str;

    public String prepay_id;

    public String sign;

    public String timestamp;

    public String merchant_name;
    public String merchantId;
    public String applicationID;
    public String productName;
    public String productDesc;
    public String requestId;
    public String urlver;
    public String url;
    public int sdkChannel;

    @Override
    public String toString() {
        return "FZStrateBean{" +
                "order_id='" + order_id + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", amount='" + amount + '\'' +
                ", return_url='" + return_url + '\'' +
                ", type='" + type + '\'' +
                ", alipay_pid='" + alipay_pid + '\'' +
                ", alipay_account='" + alipay_account + '\'' +
                ", alipay_public_key='" + alipay_public_key + '\'' +
                ", alipay_private_key='" + alipay_private_key + '\'' +
                ", wx_app_id='" + wx_app_id + '\'' +
                ", wx_mch_account='" + wx_mch_account + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
