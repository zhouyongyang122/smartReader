package com.qudiandu.smartdub.base.view;

/**
 * Created by ZY on 17/3/15.
 */

public interface ZYIListView {

    void showList(boolean isHasMore);

    void showEmpty();

    void showError();

    void showLoading();
}
