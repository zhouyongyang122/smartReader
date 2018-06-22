package com.qudiandu.smartdub.thirdParty.pay;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZY on 18/3/1.
 */

public class SRAliPay {

    private SRAliPayCallBack callBack;

    public interface SRAliPayCallBack {
        /**
         * @param result: 1支付成功; 2支付结果待确认; 3支持失败; 4检查结果
         * @return void
         * @function onResult
         * @Description
         * @date 2015年7月24日下午4:31:37
         */
        void onAliPayResult(int result, String msg);
    }

    public void sendPayReq(final Activity activity,
                           SRAliPayCallBack callBack_,
                           String alipay_private_key,
                           String alipay_pid,
                           String alipay_account,
                           String order_id,
                           String title,
                           String desc,
                           String amount,
                           String return_url) {
        this.callBack = callBack_;
        String orderInfo = getOrderInfo(alipay_pid, alipay_account, order_id, title, desc, amount, return_url);

        // 订单做RSA签名
        String sign = sign(orderInfo, alipay_private_key);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object object) {
                FZAliPayResult aliPayResult = new FZAliPayResult(object != null ? object.toString() : null);

                String resultStatus = aliPayResult.resultStatus;

                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    if (null != callBack) {
                        callBack.onAliPayResult(1, "支付成功");
                    }
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        if (null != callBack) {
                            callBack.onAliPayResult(2, "支付结果确认中");
                        }
                    } else if(TextUtils.equals(resultStatus, "6001")){
                        if (null != callBack) {
                            callBack.onAliPayResult(5, "支付被取消");
                        }
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        if (null != callBack) {
                            callBack.onAliPayResult(3, "支付失败");
                        }
                    }
                }
            }
        });

    }

    /**
     * @return String
     * @function getOrderInfo
     * @Description create the order info. 创建订单信息
     * @date 2015年6月10日下午6:21:48
     */
    public String getOrderInfo(String alipay_pid,
                               String alipay_account,
                               String order_id,
                               String title,
                               String desc,
                               String amount,
                               String return_url) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + alipay_pid + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + alipay_account + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order_id + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + title + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + desc + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + amount + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + return_url + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        System.out.print(orderInfo);
        return orderInfo;
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion(Activity activity) {
        PayTask payTask = new PayTask(activity);
        String version = payTask.getVersion();
        Toast.makeText(activity, version, Toast.LENGTH_SHORT).show();
    }


    private String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update(content.getBytes("UTF-8"));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private class FZAliPayResult {

        public String resultStatus;
        public String result;
        public String memo;

        private String gatValue(String content, String key) {
            String prefix = key + "={";
            return content.substring(content.indexOf(prefix) + prefix.length(), content.lastIndexOf("}"));
        }

        public FZAliPayResult(String rawResult) {
            if (TextUtils.isEmpty(rawResult)) {
                return;
            }

            String[] resultParams = rawResult.split(";");
            for (String resultParam : resultParams) {
                if (resultParam.startsWith("resultStatus")) {
                    resultStatus = gatValue(resultParam, "resultStatus");
                }
                if (resultParam.startsWith("result")) {
                    result = gatValue(resultParam, "result");
                }
                if (resultParam.startsWith("memo")) {
                    memo = gatValue(resultParam, "memo");
                }
            }
        }
    }
}
