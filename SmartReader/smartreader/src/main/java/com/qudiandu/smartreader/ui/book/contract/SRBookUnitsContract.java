package com.qudiandu.smartreader.ui.book.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.main.model.bean.SRCatalogue;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/9/7.
 */

public interface SRBookUnitsContract {

    interface IView extends ZYIBaseView<IPresenter> {

        void showList(List<SRCatalogue> catalogues);

        void toDubbing(ArrayList<SRTract> tracts, String bookId, SRCatalogue catalogue);
    }

    interface IPresenter extends ZYIBasePresenter {
//        void toDubbing(int cateId);
        void toDubbing(SRCatalogue catalogue);
    }
}
