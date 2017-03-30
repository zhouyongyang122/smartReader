package com.smartreader.service.downNet.down;

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
        if (downloadScriberListener.get() != null) {
            downloadScriberListener.get().onStart();
        }
        downEntity.setState(ZYDownState.START);
    }

    @Override
    public void onCompleted() {
        if (downloadScriberListener.get() != null) {
            downloadScriberListener.get().onComplete(downEntity);
        }
        ZYDownloadManager.getInstance().removeEntity(downEntity);
        downEntity.setState(ZYDownState.FINISH);
        downEntity.update();
    }

    @Override
    public void onError(Throwable e) {
        if (downloadScriberListener.get() != null) {
            downloadScriberListener.get().onError(e.getMessage());
        }
        ZYDownloadManager.getInstance().removeEntity(downEntity);
        downEntity.setState(ZYDownState.ERROR);
        downEntity.update();
    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void update(long current, long total, boolean done) {
        if (downEntity.getTotal() > total) {
            current = downEntity.getTotal() - total + current;
        } else {
            downEntity.setTotal(total);
        }
        downEntity.setCurrent(current);
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
                            downEntity.setState(ZYDownState.DOWNING);
                            downloadScriberListener.get().updateProgress(aLong, downEntity.getTotal());
                        }
                    });
        }
    }
}
