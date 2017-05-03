package com.smartreader.ui.main.presenter;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYListDataContract;
import com.smartreader.base.mvp.ZYListDataPresenter;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.main.contract.SRBookListContract;
import com.smartreader.ui.main.model.SRAddBookManager;
import com.smartreader.ui.main.model.SRMainModel;
import com.smartreader.ui.main.model.bean.SRBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookListPresenter extends ZYListDataPresenter<SRBookListContract.IView, SRMainModel, SRBook> implements SRBookListContract.IPresenter {

    private String gradeId;

    public SRBookListPresenter(SRBookListContract.IView view, SRMainModel model, String gradeId) {
        super(view, model);
        this.gradeId = gradeId;
        mRows = 40;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getBooks(gradeId), new ZYNetSubscriber<ZYResponse<List<SRBook>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRBook>> response) {
                if (response.data != null) {
                    List<SRBook> books = new ArrayList<SRBook>();
                    for (SRBook book : response.data) {
                        if (SRBook.queryById(book.book_id) == null) {
                            books.add(book);
                        }
                    }
                    response.data = books;
                }
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    @Override
    public void reportAddBookts() {
        List<SRBook> books = SRAddBookManager.getInstance().getAddBooks();
        StringBuffer buffer = new StringBuffer();
        for (SRBook book : books) {
            buffer.append(book.book_id + ",");
        }
        if (buffer.length() > 0) {
            String bookIds = buffer.toString();
            bookIds = bookIds.substring(0, bookIds.length() - 1);
            mSubscriptions.add(ZYNetSubscription.subscription(mModel.bookAddReport(bookIds), new ZYNetSubscriber() {
            }));
        }
    }
}
