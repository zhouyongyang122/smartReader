package com.qudiandu.smartdub.ui.wordStudy.presenter;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;
import com.qudiandu.smartdub.ui.wordStudy.contract.SRWordStudyUnitsContract;
import com.qudiandu.smartdub.ui.wordStudy.model.SRWordStudyModel;
import com.qudiandu.smartdub.ui.wordStudy.model.bean.SRWordStudyUnit;

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

    public SRBook getBook() {
        return mBook;
    }
}
