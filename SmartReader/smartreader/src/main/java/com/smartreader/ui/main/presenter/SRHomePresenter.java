package com.smartreader.ui.main.presenter;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.service.downNet.down.ZYDownState;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.main.contract.SRHomeContract;
import com.smartreader.ui.main.model.SRMainModel;
import com.smartreader.ui.main.model.bean.SRAdert;
import com.smartreader.ui.main.model.bean.SRBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/3/28.
 */

public class SRHomePresenter extends ZYBasePresenter implements SRHomeContract.IPresenter {

    private SRHomeContract.IView iView;

    private SRMainModel model;

    private List<SRAdert> aderts;

    private List<SRBook> books = new ArrayList<SRBook>();

    public SRHomePresenter(SRHomeContract.IView iView) {
        this.iView = iView;
        model = new SRMainModel();
        this.iView.setPresenter(this);
    }

    public void loadBooks() {
        List<SRBook> results = SRBook.queryAll();
        books.clear();

        if (results != null && results.size() > 0) {
            books.addAll(results);
        }

        for (SRBook book : results) {
            if (book.getBook_id_int() <= 0) {
                continue;
            }
            if (book.getState().getState() == ZYDownState.UNZIP.getState()) {
                book.setState(ZYDownState.UNZIPERROR);
            } else if (book.getState().getState() != ZYDownState.UNZIPERROR.getState() && book.getState().getState() != ZYDownState.ERROR.getState()) {
                book.setState(ZYDownState.PAUSE);
            }
        }

        SRBook defBook = new SRBook();
        defBook.setBook_id("-1");
        books.add(defBook);
        iView.showBooks(books);
    }

    @Override
    public void subscribe() {
        iView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(model.getAdverts(""), new ZYNetSubscriber<ZYResponse<List<SRAdert>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRAdert>> response) {
                iView.hideProgress();
                if (response.data != null && response.data.size() > 0) {
                    aderts = response.data;
                    iView.showAderts(aderts);
                }
            }

            @Override
            public void onFail(String message) {
                iView.hideProgress();
//                super.onFail(message);
            }
        }));
    }

    public List<SRAdert> getAderts() {
        return aderts;
    }

    public List<SRBook> getBooks() {
        return books;
    }
}
