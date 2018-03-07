package com.qudiandu.smartreader.ui.rank.contract;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;

/**
 * Created by ZY on 18/3/6.
 */

public interface SRRankContract {

    interface IView extends ZYListDataContract.View<IPresenter>{

    }

    interface IPresenter extends ZYListDataContract.Presenter<SRRank>{
        void setTimeType(int timeType);
    }
}
