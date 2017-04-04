package com.smartreader.base.mvp;

import java.util.UUID;

/**
 * Created by ZY on 17/3/14.
 */

public interface ZYIBaseView<P> {

    void setPresenter(P presenter);

    void showProgress();

    void hideProgress();

    void showToast(String msg);

    void finish();

    void showLoading();

    void hideLoading();

    void showError();
}
