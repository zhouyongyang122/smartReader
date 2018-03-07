package com.qudiandu.smartreader.ui.vip.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.base.view.ZYIListView;
import com.qudiandu.smartreader.ui.vip.model.bean.SRVip;

import java.util.List;

/**
 * Created by ZY on 18/3/6.
 */

public interface SRVipContract {

    interface IView extends ZYIBaseView<IPresenter>, ZYIListView {

        void buySuccess();

        void buyFail();
    }

    interface IPresenter extends ZYIBasePresenter {

        void loadVip();

        void buy(SRVip.Price price, int payType);

        SRVip getVip();

        List<SRVip.Rights> getRightsList();
    }

}
