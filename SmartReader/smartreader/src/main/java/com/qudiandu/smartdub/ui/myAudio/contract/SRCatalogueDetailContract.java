package com.qudiandu.smartdub.ui.myAudio.contract;

import com.qudiandu.smartdub.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartdub.base.mvp.ZYIBaseView;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueDetail;

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
