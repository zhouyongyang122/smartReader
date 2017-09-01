package com.qudiandu.smartreader.service.downNet.down;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.qudiandu.smartreader.utils.ZYFileUtils;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYUrlUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.functions.Func1;

/**
 * Created by ZY on 17/8/30.
 */

public class ZYDownloadService extends Service {

    ZYDownloadThreadPool threadPool;

    private HashMap<String, DownloadTask> audios = new HashMap<String, DownloadTask>();

    @Override
    public void onCreate() {
        super.onCreate();
        getThreadPool();
    }

    private ZYDownloadThreadPool getThreadPool() {
        if (threadPool == null || threadPool.THREAD_POOL_EXECUTOR == null || threadPool.THREAD_POOL_EXECUTOR.isShutdown()) {
            threadPool = new ZYDownloadThreadPool(1);
        }
        return threadPool;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public boolean addBook(ZYIDownBase downBase) {
        synchronized (this) {
            if (downBase == null) {
                ZYLog.e(getClass().getSimpleName(), "addDownloadAudio: downBase is null");
                return false;
            }
            if (audios.containsKey(downBase.getId())) {
                ZYLog.e(getClass().getSimpleName(), "addDownloadAudio: downBase is downloading");
                return false;
            }
            downBase.setState(ZYDownState.WAIT);
            if (downBase.save() <= 0) {
                ZYLog.e(getClass().getSimpleName(), "addDownloadAudio: downBase save fail");
                return false;
            }
            DownloadTask task = new DownloadTask(downBase);
            audios.put(downBase.getId(), task);
            getThreadPool().execute(task);
            return true;
        }
    }

    public void addBooks(List<ZYIDownBase> downBases) {
        if (downBases == null || downBases.size() <= 0) {
            return;
        }
        for (ZYIDownBase downBase : downBases) {
            addBook(downBase);
        }
    }

    public void delBook(ZYIDownBase downBase) {
        cancleBook(downBase.getId());
        downBase.delete();
    }


    public boolean cancleBook(String id) {
        synchronized (this) {
            if (audios.containsKey(id)) {
                DownloadTask task = audios.get(id);
                task.entity.setState(ZYDownState.PAUSE);
                task.entity.update(false);
                getThreadPool().removeTask(task);
                task.unsubscribe();
                audios.remove(id);
                return true;
            }
            return false;
        }
    }

    public boolean cancleAll() {
        synchronized (this) {
            Collection<DownloadTask> tasks = audios.values();
            for (DownloadTask task : tasks) {
                task.entity.setState(ZYDownState.PAUSE);
                task.unsubscribe();
                task.entity.update(false);
            }
            audios.clear();
            getThreadPool().shutdown();
            return true;
        }
    }

    public void removeTask(String id) {
        synchronized (this) {
            audios.remove(id);
        }
    }

    public class DownloadTask implements Runnable {

        ZYIDownBase entity;

        public ZYDownloadSubscriber downloadSubscriber;

        public DownloadTask(ZYIDownBase entity) {
            this.entity = entity;
        }

        @Override
        public void run() {
            //开始下载
            downloadSubscriber = new ZYDownloadSubscriber(entity);
            ZYDownloadInterceptor interceptor = new ZYDownloadInterceptor(downloadSubscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(3000, TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(ZYUrlUtils.getBasUrl(entity.getUrl()))
                    .build();
            ZYDownloadAPI downloadService = retrofit.create(ZYDownloadAPI.class);
            downloadService.download("bytes=" + entity.getCurrent() + "-", entity.getUrl())
                    .map(new Func1<ResponseBody, ZYIDownBase>() {
                        @Override
                        public ZYIDownBase call(ResponseBody responseBody) {
                            try {
                                ZYFileUtils.writeResponseBodyCache(responseBody, new File(entity.getSavePath()), entity.getCurrent(), entity.getTotal());
                            } catch (Exception e) {
                            /*失败抛出异常*/
                                throw new RuntimeException(e.getMessage());
                            }
                            return entity;
                        }
                    })
                    .subscribe(downloadSubscriber);
        }

        public void unsubscribe() {
            try {
                downloadSubscriber.unsubscribe();
            } catch (Exception e) {

            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }

    public class DownloadBinder extends Binder {
        //返回Service对象
        public ZYDownloadService getService() {
            return ZYDownloadService.this;
        }
    }
}
