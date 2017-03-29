package com.smartreader.service.net;

import com.smartreader.SRApplication;
import com.smartreader.base.bean.ZYResponse;
import com.smartreader.utils.ZYLog;
import com.smartreader.utils.ZYToast;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYNetSubscriber<R extends ZYResponse> extends rx.Subscriber<R> {

    public static String errorMsg = "网络异常,请重新尝试";

    @Override
    /**
     * 事件开始前的处理
     */
    public void onStart() {
        super.onStart();
        //可以处理token失效的问题
    }

    @Override
    /**
     * 事件完成 子类不要继承
     */
    public void onCompleted() {
    }

    @Override
    /**
     * 事件出错 子类不要继承
     */
    public void onError(final Throwable e) {
        if (e != null) {
            ZYLog.e(e.getMessage());
        }
        onFail(errorMsg);
    }

    @Override
    /**
     * 事件响应 子类不要继承
     */
    public void onNext(R response) {
        if (response != null) {
            switch (response.status) {
                case ZYResponse.STATUS_SUCCESS:
                    onSuccess(response);
                    break;
                case ZYResponse.STATUS_403:
                    //token失效
                    break;
                case ZYResponse.STATUS_FAIL:
                    onFail(response.msg);
                    break;
                default:
                    onOtherResponse(response.status, response.msg);
                    break;
            }
        } else {
            onFail(errorMsg);
        }
    }

    /**
     * 请求成功 子类继承
     *
     * @param response
     */
    public void onSuccess(R response) {

    }

    /**
     * 请求失败,子类继承
     *
     * @param message
     */
    public void onFail(String message) {
        if (message != null) {
            ZYToast.show(SRApplication.getInstance(), message);
        } else {
            ZYToast.show(SRApplication.getInstance(), errorMsg);
        }
    }

    /**
     * 其它响应处理(服务器返回的status不为0且不为1的响应),子类继承
     *
     * @param status
     */
    public void onOtherResponse(int status, String message) {
        onFail(message);
    }
}
