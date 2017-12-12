package com.qudiandu.smartreader.ui.wordStudy.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.wordStudy.contract.SRWordStudyUnitsContract;
import com.qudiandu.smartreader.ui.wordStudy.model.SRWordStudyModel;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyUnit;

import java.util.List;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyUnitsPresenter extends ZYListDataPresenter<SRWordStudyUnitsContract.IView, SRWordStudyModel, SRWordStudyUnit> implements SRWordStudyUnitsContract.IPresenter {

    SRBook mBook;

    public SRWordStudyUnitsPresenter(SRWordStudyUnitsContract.IView view, SRBook book) {
        super(view, new SRWordStudyModel());
        mBook = book;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getBookUnits(mBook.book_id), new ZYNetSubscriber<ZYResponse<List<SRWordStudyUnit>>>() {

            @Override
            public void onSuccess(ZYResponse<List<SRWordStudyUnit>> response) {
                super.onSuccess(response);
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }
}
