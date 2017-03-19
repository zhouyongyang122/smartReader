package com.smartreader.base.mvp;

import java.util.UUID;

/**
 * Created by ZY on 17/3/14.
 */

public interface SRIBasePresenter {

    //界面显示事件
    public static final String IBASE_PRESENTER_ACTION_RESUME = UUID.randomUUID().toString();
    //界面暂停事件
    public static final String IBASE_PRESENTER_ACTION_PAUSE = UUID.randomUUID().toString();
    //界面销毁事件
    public static final String IBASE_PRESENTER_ACTION_DESTORY = UUID.randomUUID().toString();

    void subscribe();

    void presenterAction(Object message, String action);

    void unsubscribe();
}
