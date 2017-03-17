package com.smartreader.common.view;

import android.view.View;

/**
 * Created by ZY on 17/3/16.
 */

public interface SRILoadMoreView {

    View getView();

    void showLoading();

    void showNoMore();

    void showError();

    void setNoMoreText(String text);

    void hide();
}
