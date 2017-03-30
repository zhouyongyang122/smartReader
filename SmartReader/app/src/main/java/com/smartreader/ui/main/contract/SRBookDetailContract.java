package com.smartreader.ui.main.contract;

import com.smartreader.base.mvp.ZYIBasePresenter;
import com.smartreader.base.mvp.ZYIBaseView;
import com.smartreader.ui.main.model.bean.SRBook;

/**
 * Created by ZY on 17/3/29.
 */

public interface SRBookDetailContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void showBookData(SRBook bookData);
    }

    interface IPresenter extends ZYIBasePresenter {
        SRBook getBookData();

        String getLocalRootDirPath();
    }
}
