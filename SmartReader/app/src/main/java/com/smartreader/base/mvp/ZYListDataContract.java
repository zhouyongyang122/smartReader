package com.smartreader.base.mvp;

import com.smartreader.base.view.ZYIListView;

import java.util.List;

/**
 * Created by ZY on 17/3/30.
 */

public interface ZYListDataContract {

    interface View<P> extends ZYIBaseView<P>, ZYIListView {

    }

    interface Presenter<D> extends ZYIBasePresenter {

        void refresh();

        void loadMore();

        List<D> getDataList();
    }
}
