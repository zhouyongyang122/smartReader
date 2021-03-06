package com.qudiandu.smartreader.ui.main.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdert;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;

import java.util.List;

/**
 * Created by ZY on 17/3/28.
 */

public interface SRHomeContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void showAderts(List<SRAdert> aderts);

        void showBook(SRBook book);
    }

    interface IPresenter extends ZYIBasePresenter {

        void loadBook();

        List<SRAdert> getAderts();

        SRBook getBook();
    }
}
