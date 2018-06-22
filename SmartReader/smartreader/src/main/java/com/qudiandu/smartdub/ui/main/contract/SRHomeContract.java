package com.qudiandu.smartdub.ui.main.contract;

import com.qudiandu.smartdub.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartdub.base.mvp.ZYIBaseView;
import com.qudiandu.smartdub.ui.main.model.bean.SRAdert;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;

import java.util.List;

/**
 * Created by ZY on 17/3/28.
 */

public interface SRHomeContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void showBook(SRBook book);
    }

    interface IPresenter extends ZYIBasePresenter {

        void loadBook();

        SRBook getBook();
    }
}
