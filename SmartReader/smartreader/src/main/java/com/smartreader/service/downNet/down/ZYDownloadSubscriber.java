package com.smartreader.service.downNet.down;

import com.smartreader.utils.ZYLog;

import java.lang.ref.SoftReference;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ZY on 17/3/17.
 */

public class ZYDownloadSubscriber<T> extends Subscriber<T> implements ZYDownloadProgressListener {

    private SoftReference<ZYDownloadScriberListener> downloadScriberListener;

    private ZYIDownBase downEntity;


    public ZYDownloadSubscriber(ZYIDownBase downEntity) {
        this.downloadScriberListener = new SoftReference<>(downEntity.getListener());
        this.downEntity = downEntity;
    }

    public void setDownInfo(ZYIDownBase downEntity) {
        this.downloadScriberListener = new SoftReference<>(downEntity.getListener());
        this.downEntity = downEntity;
    }

    @Override
    public void onStart() {
        downEntity.setState(ZYDownState.START);
        if (downloadScriberListener.get() != null) {
            downloadScriberListener.get().onStart();
        }
    }

    @Override
    public void onCompleted() {
        ZYDownloadManager.getInstance().removeEntity(downEntity);
        downEntity.setState(ZYDownState.FINISH);
        downEntity.update();
        if (downloadScriberListener.get() != null) {
            downloadScriberListener.get().onComplete(downEntity);
        }
    }

    @Override
    public void onError(Throwable e) {
        ZYDownloadManager.getInstance().removeEntity(downEntity);
        if (downEntity.getCurrent() > 0 && downEntity.getCurrent() >= downEntity.getTotal()) {
            downEntity.setState(ZYDownState.UNZIPERROR);
        } else {
            downEntity.setState(ZYDownState.ERROR);
        }
        downEntity.update();
        if (downloadScriberListener.get() != null) {
            downloadScriberListener.get().onError(e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void update(long current, long total, boolean done) {
        downEntity.setTotal(total);
        downEntity.setCurrent(current);
        if (current > 0 && current >= total) {
            downEntity.setState(ZYDownState.UNZIP);
        } else {
            downEntity.setState(ZYDownState.DOWNING);
        }
        downEntity.update();
        if (downloadScriberListener.get() != null) {
            //是否考虑阻塞问题
            rx.Observable.just(current).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            if (downEntity.getState() == ZYDownState.PAUSE || downEntity.getState() == ZYDownState.STOP) {
                                return;
                            }
                            downloadScriberListener.get().updateProgress(aLong, downEntity.getTotal());
                        }
                    });
        }
    }
}
