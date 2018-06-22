package com.qudiandu.smartdub.ui.book.contract;

import com.qudiandu.smartdub.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartdub.base.mvp.ZYIBaseView;
import com.qudiandu.smartdub.base.view.ZYIListView;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;

import java.util.List;

/**
 * Created by ZY on 17/8/31.
 */

public interface SRBooksContract {

    interface IView extends ZYIBaseView<IPresenter>, ZYIListView {

    }

    interface IPresenter extends ZYIBasePresenter {

        void setEdit(boolean isEdit);

        int getClassId();

        void loadDatas();

        List<SRBook> getDatas();
    }
}
