package com.qudiandu.smartdub.ui.mark.contract;

import com.qudiandu.smartdub.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartdub.base.mvp.ZYIBaseView;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartdub.ui.dubbing.model.bean.SRMarkBean;

import java.util.ArrayList;

/**
 * Created by ZY on 17/4/2.
 */

public interface SRMarkContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void uploadAudioSuc(SRMarkBean markBean);
        void uploadMergeAudioSuc(SRCatalogueResponse response);
    }

    interface IPresenter extends ZYIBasePresenter {

        String getGroupId();

        void uploadTractAudio(SRTract tract);

        ArrayList<SRTract> getTracks();

        void uploadMergedTractAudio();
    }
}
