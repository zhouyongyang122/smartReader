package com.qudiandu.smartdub.ui.main.presenter;

import com.qudiandu.smartdub.ZYPreferenceHelper;
import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYBasePresenter;
import com.qudiandu.smartdub.service.downNet.down.ZYDownState;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.main.contract.SRHomeContract;
import com.qudiandu.smartdub.ui.main.model.SRMainModel;
import com.qudiandu.smartdub.ui.main.model.bean.SRAdert;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/3/28.
 */

public class SRHomePresenter extends ZYBasePresenter implements SRHomeContract.IPresenter {

    private SRHomeContract.IView mView;

    private SRBook mBook;

    public SRHomePresenter(SRHomeContract.IView iView) {
        mView = iView;
        mView.setPresenter(this);
    }

    public void loadBook() {
        mBook = SRBook.queryById(ZYPreferenceHelper.getInstance().getSelectBookId(0) + "");
        if (mBook == null) {
            mBook = SRBook.queryById("0");
        }
        mView.showBook(mBook);
    }

    public SRBook getBook() {
        return mBook;
    }
}
