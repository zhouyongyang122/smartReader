package com.qudiandu.smartdub.ui.main.contract;

import com.qudiandu.smartdub.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartdub.base.mvp.ZYIBaseView;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;

import java.util.ArrayList;

/**
 * Created by ZY on 17/3/29.
 */

public interface SRBookHomeContract {

    interface IView extends ZYIBaseView<IPresenter> {

        void showBookData(SRBook bookData);

        void selRepeatsEnd();

        void playRepeats();
    }

    interface IPresenter extends ZYIBasePresenter {

        void onSelecteTrack(SRTract tract);

        ArrayList<SRTract> getTractsByCatalogueId(int catalogueId);

        boolean isRepeats();

        void setRepeats(boolean repeats);

        boolean isSingleRepeat();

        void setSingleRepeat(boolean singleRepeat);

        boolean isNeedShowSentenceBg();

        void setNeedShowSentenceBg(boolean needShowSentenceBg);

        SRBook getBookData();

        String getLocalRootDirPath();

        void stopSingleRepeat();

        void stopRepeats();

        void puaseRepeats();

        void continueRepeats();
    }
}
