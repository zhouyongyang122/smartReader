package com.qudiandu.smartreader.ui.mark.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public interface SRMarkContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void uploadAudioSuc(SRMarkBean markBean);
        void uploadMergeAudioSuc(SRCatalogueResponse response);
    }

    interface IPresenter extends ZYIBasePresenter {

        void uploadTractAudio(SRTract tract);

        ArrayList<SRTract> getTracks();

        void uploadMergedTractAudio();
    }
}
