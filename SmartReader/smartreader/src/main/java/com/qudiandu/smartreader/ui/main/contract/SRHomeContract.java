package com.qudiandu.smartreader.ui.main.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdvert;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;

/**
 * Created by ZY on 17/3/28.
 */

public interface SRHomeContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void showBook(SRBook book);
        void showFloatAd(final SRAdvert advert);
    }

    interface IPresenter extends ZYIBasePresenter {

        void loadBook();

        SRBook getBook();

        void loadAdvert();
    }
}
