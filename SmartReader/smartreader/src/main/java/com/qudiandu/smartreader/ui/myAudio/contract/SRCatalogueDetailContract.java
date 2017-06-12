package com.qudiandu.smartreader.ui.myAudio.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueDetail;

/**
 * Created by ZY on 17/6/10.
 */

public interface SRCatalogueDetailContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void showCatalogueData(SRCatalogueDetail data);
    }

    interface IPresenter extends ZYIBasePresenter {
        void onSelecteTrack(SRTract tract);
        void loadData();
        SRCatalogueDetail getCatalogueDetail();
    }
}
