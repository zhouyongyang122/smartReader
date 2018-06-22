package com.qudiandu.smartdub.service.downNet.down;

import android.view.View;

import com.qudiandu.smartdub.base.event.ZYEventDowloadUpdate;
import com.qudiandu.smartdub.ui.main.model.SRBookFileManager;
import com.qudiandu.smartdub.utils.ZYLog;
import com.qudiandu.smartdub.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;

/**
 * Created by ZY on 17/3/17.
 */

public class ZYDownloadSubscriber<T> extends Subscriber<T> implements ZYDownloadProgressListener {

    private ZYIDownBase downEntity;

    public ZYDownloadSubscriber(ZYIDownBase downEntity) {
        this.downEntity = downEntity;
    }

    @Override
    public void onStart() {
        ZYLog.e(getClass().getSimpleName(), "onStart: " + downEntity.getUrl());
        downEntity.setState(ZYDownState.START);
        downEntity.save();
        EventBus.getDefault().post(new ZYEventDowloadUpdate(downEntity));
    }

    @Override
    public void onCompleted() {
        ZYLog.e(getClass().getSimpleName(), "onCompleted: " + downEntity.getCurrent() + ":" + downEntity.getTotal());
        ZYLog.e(getClass().getSimpleName(), "准备解压");
        ZYDownloadManager.getInstance().removeTask(downEntity.getId());
//        downEntity.setState(ZYDownState.FINISH);
        downEntity.setCurrent(downEntity.getTotal());
        downEntity.setState(ZYDownState.UNZIP);
        downEntity.update(false);
        EventBus.getDefault().post(new ZYEventDowloadUpdate(downEntity));
        unzip();
    }

    @Override
    public void onError(Throwable e) {
        ZYLog.e(getClass().getSimpleName(), "onError: " + e.getMessage());
        ZYDownloadManager.getInstance().removeTask(downEntity.getId());
        if (downEntity.getState() != ZYDownState.PAUSE) {
            downEntity.setState(ZYDownState.ERROR);
        }
        downEntity.update(false);
        EventBus.getDefault().post(new ZYEventDowloadUpdate(downEntity));
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
        if (done) {
            ZYLog.e(getClass().getSimpleName(), "update: " + current + ":" + downEntity.getTotal() + ":" + done);
        } else {
            if ((current - current % 1024) % (100 * 1024) == 0) {
                ZYLog.e(getClass().getSimpleName(), "update: " + current + ":" + downEntity.getTotal() + ":" + done);
            }
        }
//        if (done) {
//            ZYLog.e(getClass().getSimpleName(), "准备解压");
//            downEntity.setState(ZYDownState.UNZIP);
//            downEntity.update(false);
//            EventBus.getDefault().post(new ZYEventDowloadUpdate(downEntity));
//            unzip();
//        } else {
        downEntity.setState(ZYDownState.DOWNING);
        downEntity.update(false);
        EventBus.getDefault().post(new ZYEventDowloadUpdate(downEntity));
//        }
    }

    private void unzip() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SRBookFileManager.unZip(SRBookFileManager.getBookZipPath(downEntity.getId()), SRBookFileManager.getBookPath(downEntity.getId()), new SRBookFileManager.UnZipListener() {
                    @Override
                    public void unZipSuccess() {
                        ZYLog.e(getClass().getSimpleName(), "解压成功");
                        downEntity.setSavePath(SRBookFileManager.getBookPath(downEntity.getId()));
                        downEntity.setState(ZYDownState.FINISH);
                        downEntity.update(false);
                        EventBus.getDefault().post(new ZYEventDowloadUpdate(downEntity));
                    }

                    @Override
                    public void unZipError() {
                        ZYLog.e(getClass().getSimpleName(), "解压出错");
                        downEntity.setState(ZYDownState.UNZIPERROR);
                        downEntity.update(false);
                        EventBus.getDefault().post(new ZYEventDowloadUpdate(downEntity));
                    }
                });
            }
        }.start();
    }
}
