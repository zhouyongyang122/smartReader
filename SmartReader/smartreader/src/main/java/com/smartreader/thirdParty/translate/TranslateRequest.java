package com.smartreader.thirdParty.translate;

import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.utils.ZYLog;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhouyongyang on 17/1/18.
 */

public class TranslateRequest {

    private static TranslateRequest request;

    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    private TranslateRequestCallBack requestCallBack;

    private YouDaoRequestApi youDaoReuqestApi;

    private TranslateRequest() {

    }

    public static TranslateRequest getRequest() {
        if (request == null) {
            request = new TranslateRequest();
        }
        return request;
    }

    public void translate(String word, TranslateRequestCallBack callBack) {
        setRequestCallBack(callBack);
        mSubscriptions.add(ZYNetSubscription.subscription(
                getYouDaoReuqestApi()
                        .getWordDetail(
                                YouDaoRequestApi.DOCTYPE
                                , YouDaoRequestApi.KEY
                                , YouDaoRequestApi.TYPE
                                , YouDaoRequestApi.VERSION
                                , word)
                , new Subscriber<YouDaoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (requestCallBack != null) {
                            requestCallBack.translateCallBack(null, "网络出错,请重新尝试!");
                        }
                    }

                    @Override
                    public void onNext(YouDaoBean fzYouDaoBean) {
                        if (fzYouDaoBean != null && fzYouDaoBean.basic != null) {
                            if (requestCallBack != null) {
                                requestCallBack.translateCallBack(fzYouDaoBean, null);
                            }
                        } else {
                            if (requestCallBack != null) {
                                requestCallBack.translateCallBack(null, "词库暂时还没有翻译");
                            }
                        }
                    }
                }));
    }

    public void unsubscribe() {
        try {
            mSubscriptions.unsubscribe();
            mSubscriptions = new CompositeSubscription();
        } catch (Exception e) {

        }
    }

    public interface TranslateRequestCallBack {
        void translateCallBack(YouDaoBean translateBean, String errorMsg);
    }

    public void setRequestCallBack(TranslateRequestCallBack requestCallBack) {
        unsubscribe();
        this.requestCallBack = requestCallBack;
    }

    /**
     * 获取翻译请求api
     *
     * @return
     */
    private YouDaoRequestApi getYouDaoReuqestApi() {
        if (youDaoReuqestApi == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor(new ZYLog())
                            .setLevel(HttpLoggingInterceptor.Level.BODY));
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl("http://fanyi.youdao.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            youDaoReuqestApi = retrofit.create(YouDaoRequestApi.class);
        }
        return youDaoReuqestApi;
    }
}
