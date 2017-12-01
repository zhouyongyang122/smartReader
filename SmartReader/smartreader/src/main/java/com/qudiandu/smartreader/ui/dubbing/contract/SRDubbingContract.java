package com.qudiandu.smartreader.ui.dubbing.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkBean;

import java.util.ArrayList;

/**
 * Created by ZY on 17/12/1.
 */

public interface SRDubbingContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void uploadAudioSuc(SRMarkBean markBean);
        void uploadMergeAudioSuc(SRCatalogueResponse response);
    }

    interface IPresenter extends ZYIBasePresenter {

        int getPageId();

        String getGroupId();

        void uploadTractAudio();

        SRTract getTrack();

        String getCatalogueId();

        String getTaskId();
    }
}
