package com.qudiandu.smartdub.ui.book.presenter;

import com.qudiandu.smartdub.base.mvp.ZYBasePresenter;
import com.qudiandu.smartdub.ui.book.contract.SRBooksContract;
import com.qudiandu.smartdub.ui.book.model.SRBookModel;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/8/31.
 */

public class SRBooksPresenter extends ZYBasePresenter implements SRBooksContract.IPresenter {

    SRBooksContract.IView mView;

    SRBookModel mMode;

    int mClassId;

    boolean mIsEdit;

    public List<SRBook> mDatas = new ArrayList<SRBook>();

    public SRBooksPresenter(SRBooksContract.IView view, int classId) {
        mView = view;
        mMode = new SRBookModel();
        mView.setPresenter(this);
        mClassId = classId;
    }

    public void loadDatas() {
        List<SRBook> results = SRBook.queryByClassId(mClassId + "");
        mDatas.clear();
        if (results != null && results.size() > 0) {
            mDatas.addAll(results);
        }
        SRBook addBook = new SRBook();
        addBook.setBook_id("-1");
        mDatas.add(addBook);
        mView.showList(false);
    }

    public void setEdit(boolean isEdit) {
        mIsEdit = isEdit;
        for (SRBook book : mDatas) {
            book.isDeleteStatus = mIsEdit;
            if (book.getBook_id_int() > 0) {
                book.isCanDelete = mIsEdit;
            } else {
                book.isCanDelete = false;
            }
        }
        mView.showList(false);
    }

    public List<SRBook> getDatas() {
        return mDatas;
    }

    public int getClassId() {
        return mClassId;
    }
}
