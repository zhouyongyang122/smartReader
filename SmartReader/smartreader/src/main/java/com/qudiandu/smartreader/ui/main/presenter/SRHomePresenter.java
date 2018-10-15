package com.qudiandu.smartreader.ui.main.presenter;

import com.qudiandu.smartreader.ZYPreferenceHelper;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.contract.SRHomeContract;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdvert;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;

import java.util.List;

/**
 * Created by ZY on 17/3/28.
 */

public class SRHomePresenter extends ZYBasePresenter implements SRHomeContract.IPresenter {

    private SRHomeContract.IView mView;

    private SRBook mBook;

    private SRMainModel mMode;

    public SRHomePresenter(SRHomeContract.IView iView) {
        mView = iView;
        mView.setPresenter(this);
        mMode = new SRMainModel();
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

    @Override
    public void loadAdvert() {
        mSubscriptions.add(ZYNetSubscription.subscription(mMode.getAdverts("2"), new ZYNetSubscriber<ZYResponse<List<SRAdvert>>>() {

            @Override
            public void onSuccess(ZYResponse<List<SRAdvert>> response) {
                if (response.data != null && response.data.size() > 0) {
                    mView.showFloatAd(response.data.get(0));
                }
            }

            @Override
            public void onFail(String message) {

            }
        }));
    }
}
