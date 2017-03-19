package com.smartreader.service.zyNet.down;

/**
 * Created by ZY on 17/3/17.
 * 下载回调
 */

public interface ZYDownloadScriberListener<D> {
    /**
     * 开始下载
     */
    void onStart();

    /**
     * 完成下载
     */
    void onComplete(D t);

    /**
     * 下载进度
     */
    void updateProgress(long current, long total);

    /**
     * 失败
     */
    void onError(String message);

    /**
     * 暂停下载
     */
    void onPuase();

    /**
     * 停止下载销毁
     */
    void onStop();
}
