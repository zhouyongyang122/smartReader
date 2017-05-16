package com.qudiandu.smartreader.base.mvp;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.utils.ZYToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/3/30.
 */

public abstract class ZYListDataPresenter<V extends ZYListDataContract.View, M, D> extends ZYBasePresenter
        implements ZYListDataContract.Presenter<D> {

    protected V mView;
    protected M mModel;

    protected List<D> mDataList = new ArrayList<>();

    protected int mStart;
    protected int mRows = 10;

    protected boolean isFristLoad = true;

    public ZYListDataPresenter(V view, M model) {
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        super.subscribe();
        mView.showLoading();
        refresh();
    }

    @Override
    public void refresh() {
        mStart = 0;
        loadData();
    }

    @Override
    public void loadMore() {
        mStart += mRows;
        loadData();
    }

    @Override
    public List<D> getDataList() {
        return mDataList;
    }

    protected abstract void loadData();

    protected void success(ZYResponse<List<D>> response) {
        isFristLoad = false;
        List<D> dataList = response.data;
        success(dataList);
    }

    protected void success(List<D> dataList) {
        isFristLoad = false;
        if (isRefresh()) {
            mDataList.clear();
        }
        if (dataList != null && !dataList.isEmpty()) {
            mDataList.addAll(dataList);
            mView.showList(true);
        } else if (mDataList.isEmpty()) {
            mView.showEmpty();
        } else {
            mView.showList(false);
        }
    }

    protected void fail(String message) {
        if (isFristLoad) {
            mView.showError();
        } else {
            mView.showList(true);
            ZYToast.show(SRApplication.getInstance(), message == null ? "网络异常,请重新尝试!" : message);
        }
    }

    public boolean isRefresh() {
        return mStart == 0;
    }
}
