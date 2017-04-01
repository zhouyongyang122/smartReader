package com.smartreader.base.mvp;

import java.util.UUID;

/**
 * Created by ZY on 17/3/14.
 */

public interface ZYIBasePresenter {

    void subscribe();

    void unsubscribe();

    void onResume();

    void onPause();

    void onDestroy();
}
