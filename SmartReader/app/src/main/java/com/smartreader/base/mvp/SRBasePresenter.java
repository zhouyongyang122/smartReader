package com.smartreader.base.mvp;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/3/16.
 */

public class SRBasePresenter implements SRIBasePresenter {

    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void presenterAction(Object message, String action) {

    }
}
