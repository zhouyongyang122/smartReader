package com.qudiandu.smartreader.ui.mark.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;

import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public interface SRMarkContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void uploadAudioSuc(SRMarkBean markBean);
    }

    interface IPresenter extends ZYIBasePresenter {

        String getMarkId(String tractId);

        void uploadTractAudio(SRTract tract);

        SRPage getPage();

        String getBookId();

        List<SRTract> getTracks();
    }
}
