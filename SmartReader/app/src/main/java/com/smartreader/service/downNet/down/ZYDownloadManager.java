package com.smartreader.service.downNet.down;

import com.smartreader.utils.ZYFileUtils;
import com.smartreader.utils.ZYUrlUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ZY on 17/3/17.
 */

public class ZYDownloadManager {

    private Set<ZYIDownBase> downEntities;

    private HashMap<String, ZYDownloadSubscriber> downSubscribers;

    private static ZYDownloadManager instance;

    private ZYDownloadManager() {

    }

    public static ZYDownloadManager getInstance() {
        if (instance == null) {
            synchronized (ZYDownloadManager.class) {
                if (instance == null) {
                    instance = new ZYDownloadManager();
                }
            }
        }
        return instance;
    }

    public void startDown(final ZYIDownBase entity) {
        if (entity == null || downSubscribers.get(entity.getUrl()) != null) {
            return;
        }
        ZYDownloadSubscriber downloadSubscriber = new ZYDownloadSubscriber(entity);
        downSubscribers.put(entity.getUrl(), downloadSubscriber);

        ZYDownloadService downloadService;
        if (downEntities.contains(entity)) {
            downloadService = entity.getDownloadService();
        } else {
            ZYDownloadInterceptor interceptor = new ZYDownloadInterceptor(downloadSubscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(entity.getConnectonTime(), TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(ZYUrlUtils.getBasUrl(entity.getUrl()))
                    .build();
            downloadService = retrofit.create(ZYDownloadService.class);
            entity.setDownloadService(downloadService);
            downEntities.add(entity);
        }

        downloadService.download("bytes=" + entity.getCurrent() + "-", entity.getUrl())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retryWhen(new ZYDownloadRetryFunc())
                .map(new Func1<ResponseBody, ZYIDownBase>() {
                    @Override
                    public ZYIDownBase call(ResponseBody responseBody) {
                        try {
                            ZYFileUtils.writeResponseBodyCache(responseBody, new File(entity.getSavePath()), entity.getCurrent(), entity.getTotal());
                        } catch (IOException e) {
                            /*失败抛出异常*/
                            throw new RuntimeException(e.getMessage());
                        }
                        return entity;
                    }
                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(downloadSubscriber);

    }

    public void stopDown(ZYIDownBase entity) {
        if (entity == null) {
            return;
        }
        entity.setState(ZYDownState.STOP);
        entity.getListener().onStop();
        if (downSubscribers.containsKey(entity.getUrl())) {
            ZYDownloadSubscriber subscriber = downSubscribers.get(entity.getUrl());
            subscriber.unsubscribe();
            downSubscribers.remove(entity.getUrl());
        }
        entity.update();
    }

    public void pauseDown(ZYIDownBase entity) {
        if (entity == null) {
            return;
        }
        entity.setState(ZYDownState.PAUSE);
        entity.getListener().onPuase();
        if (downSubscribers.containsKey(entity.getUrl())) {
            ZYDownloadSubscriber subscriber = downSubscribers.get(entity.getUrl());
            subscriber.unsubscribe();
            downSubscribers.remove(entity.getUrl());
        }
        entity.update();
    }

    public void stopAllDown() {
        for (ZYIDownBase entity : downEntities) {
            stopDown(entity);
        }
        downSubscribers.clear();
        downEntities.clear();
    }

    public void pauseAllDown() {
        for (ZYIDownBase entity : downEntities) {
            pauseDown(entity);
        }
        downSubscribers.clear();
        downEntities.clear();
    }

    public void removeEntity(ZYIDownBase entity) {
        downEntities.remove(entity.getUrl());
        downSubscribers.remove(entity);
    }
}
