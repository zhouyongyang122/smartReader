package com.smartreader.ui.main.contract;

import com.smartreader.base.mvp.ZYIBasePresenter;
import com.smartreader.base.mvp.ZYIBaseView;
import com.smartreader.ui.main.model.bean.SRAdert;
import com.smartreader.ui.main.model.bean.SRBook;

import java.util.List;

/**
 * Created by ZY on 17/3/28.
 */

public interface SRHomeContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void showAderts(List<SRAdert> aderts);
        void showBooks(List<SRBook> books);
    }

    interface IPresenter extends ZYIBasePresenter {
        List<SRAdert> getAderts();
        void loadBooks();
        List<SRBook> getBooks();
    }
}
