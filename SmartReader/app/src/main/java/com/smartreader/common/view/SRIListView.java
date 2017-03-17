package com.smartreader.common.view;

/**
 * Created by ZY on 17/3/15.
 */

public interface SRIListView {

    void showList(boolean isHasMore);

    void showEmpty();

    void showError();

    void showLoading();
}
