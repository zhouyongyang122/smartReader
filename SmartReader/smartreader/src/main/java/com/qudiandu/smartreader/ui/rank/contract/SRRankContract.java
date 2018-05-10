package com.qudiandu.smartreader.ui.rank.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;

import java.util.ArrayList;

/**
 * Created by ZY on 18/3/6.
 */

public interface SRRankContract {

    interface IView extends ZYListDataContract.View<IPresenter>{

    }

    interface IPresenter extends ZYListDataContract.Presenter<SRRank>{

        void setTimeType(int timeType);

        ArrayList<SRRank> getTops();

        boolean isFristLoading();
    }
}
