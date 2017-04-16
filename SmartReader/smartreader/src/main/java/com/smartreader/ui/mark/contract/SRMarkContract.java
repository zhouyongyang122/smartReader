package com.smartreader.ui.mark.contract;

import com.smartreader.base.mvp.ZYIBasePresenter;
import com.smartreader.base.mvp.ZYIBaseView;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.ui.mark.model.bean.SRMarkBean;

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
