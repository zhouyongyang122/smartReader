package com.qudiandu.smartreader.ui.book.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.base.view.ZYIListView;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;

import java.util.List;

/**
 * Created by ZY on 17/8/31.
 */

public interface SRBooksContract {

    interface IView extends ZYIBaseView<IPresenter>, ZYIListView {

    }

    interface IPresenter extends ZYIBasePresenter {

        void setEdit(boolean isEdit);

        void loadDatas();

        List<SRBook> getDatas();
    }
}
