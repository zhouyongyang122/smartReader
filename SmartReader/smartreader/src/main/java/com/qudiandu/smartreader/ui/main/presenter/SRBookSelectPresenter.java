package com.qudiandu.smartreader.ui.main.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.contract.SRBookSelectContract;
import com.qudiandu.smartreader.ui.main.model.SRBookSelectManager;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookSelectPresenter extends ZYListDataPresenter<SRBookSelectContract.IView, SRMainModel, SRBook> implements SRBookSelectContract.IPresenter {

    private String gradeId;

    boolean isTaskSel;

    private String mGradeName;

    public SRBookSelectPresenter(SRBookSelectContract.IView view, String gradeId, boolean isTaskSel,String gradeName) {
        super(view, new SRMainModel());
        this.gradeId = gradeId;
        mRows = 40;
        this.isTaskSel = isTaskSel;
        mGradeName = gradeName;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getBooks(gradeId), new ZYNetSubscriber<ZYResponse<List<SRBook>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRBook>> response) {
                if (response.data != null) {
                    if (!isTaskSel) {
                        List<SRBook> books = new ArrayList<SRBook>();
                        for (SRBook book : response.data) {
                            if (SRBook.queryById(book.book_id) == null) {
                                books.add(book);
                            }
                        }
                        response.data = books;
                    }
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
        List<SRBook> books = SRBookSelectManager.getInstance().getAddBooks();
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

    public boolean isTaskSel() {
        return isTaskSel;
    }

    public String getGradeName() {
        return mGradeName;
    }
}
