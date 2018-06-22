package com.qudiandu.smartdub.ui.vip.contract;

import com.qudiandu.smartdub.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartdub.base.mvp.ZYIBaseView;
import com.qudiandu.smartdub.base.view.ZYIListView;
import com.qudiandu.smartdub.ui.vip.model.bean.SRVip;

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

        void buy(int payType);

        SRVip.Price getSelectPrice();

        void setSelectPrice(SRVip.Price mSelectPrice);

        SRVip getVip();

        List<SRVip.Price> getPriceList();

        List<SRVip.Rights> getRightsList();
    }

}
