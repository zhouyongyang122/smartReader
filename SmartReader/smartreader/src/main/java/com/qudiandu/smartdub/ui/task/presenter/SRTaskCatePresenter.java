package com.qudiandu.smartdub.ui.task.presenter;

import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.event.SREventSelectedTask;
import com.qudiandu.smartdub.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.main.model.bean.SRTaskTitle;
import com.qudiandu.smartdub.ui.task.model.SRTaskManager;
import com.qudiandu.smartdub.ui.task.model.bean.SRTaskCate;
import com.qudiandu.smartdub.ui.task.contract.SRTaskCateContract;
import com.qudiandu.smartdub.ui.task.model.SRTaskModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/23.
 */

public class SRTaskCatePresenter extends ZYListDataPresenter<SRTaskCateContract.IView, SRTaskModel, Object> implements SRTaskCateContract.IPresenter {

    String mBookId;

    public SRTaskCatePresenter(SRTaskCateContract.IView view, String bookId) {
        super(view, new SRTaskModel());
        mBookId = bookId;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getTaskCates(mBookId, mStart, mRows), new ZYNetSubscriber<ZYResponse<List<SRTaskCate>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRTaskCate>> response) {
                super.onSuccess(response);
                List<Object> results = new ArrayList<Object>();
                if (response.data != null && response.data.size() > 0) {
                    String lastUnit = "";
                    if (mDataList.size() > 0) {
                        lastUnit = ((SRTaskCate) mDataList.get(mDataList.size() - 1)).unit;
                    } else {
                        lastUnit = response.data.get(0).unit;
                        results.add(new SRTaskTitle(lastUnit));
                    }
                    for (SRTaskCate taskCate : response.data) {
                        if (lastUnit.equals(taskCate.unit)) {
                            results.add(taskCate);
                        } else {
                            lastUnit = taskCate.unit;
                            results.add(new SRTaskTitle(lastUnit));
                            results.add(taskCate);
                        }
                    }
                }
                success(results);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    public void addTask() {
        String catalogueIds = "";
        String book_id = null;
        for (Object object : mDataList) {
            if (object instanceof SRTaskCate) {
                SRTaskCate result = (SRTaskCate) object;
                if (result.isCheck) {
                    catalogueIds += result.catalogue_id + ",";
                    book_id = result.book_id + "";
                }
            }
        }
        if (catalogueIds != null) {
//            mView.showToast(catalogueIds);
            mSubscriptions.add(ZYNetSubscription.subscription(mModel.addTask(SRTaskManager.getInstance().getCurrentTaskClassId() + "", book_id, catalogueIds), new ZYNetSubscriber() {
                @Override
                public void onSuccess(ZYResponse response) {
                    mView.showToast("任务发布成功!");
                    EventBus.getDefault().post(new SREventSelectedTask());
                    mView.finish();
                }

                @Override
                public void onFail(String message) {
                    super.onFail(message);
                }
            }));
        } else {
            mView.showToast("还没有选择任务!");
        }
    }
}
