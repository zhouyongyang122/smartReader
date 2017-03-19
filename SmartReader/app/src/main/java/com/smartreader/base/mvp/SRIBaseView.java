package com.smartreader.base.mvp;

import java.util.UUID;

/**
 * Created by ZY on 17/3/14.
 */

public interface SRIBaseView<P> {

    //显示进度条
    public static final String IBASE_VIEW_ACTION_SHOW_PROGRESS = UUID.randomUUID().toString();
    //隐藏进度条
    public static final String IBASE_VIEW_ACTION_HIDE_PROGRESS = UUID.randomUUID().toString();
    //提示信息
    public static final String IBASE_VIEW_ACTION_TOAST = UUID.randomUUID().toString();
    //结束界面
    public static final String IBASE_VIEW_ACTION_FINISH = UUID.randomUUID().toString();

    void setPresenter(P presenter);

    void viewAction(Object message, String action);
}
