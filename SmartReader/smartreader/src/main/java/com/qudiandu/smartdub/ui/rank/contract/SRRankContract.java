package com.qudiandu.smartdub.ui.rank.contract;

import com.qudiandu.smartdub.base.mvp.ZYListDataContract;
import com.qudiandu.smartdub.ui.rank.model.bean.SRRank;

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
