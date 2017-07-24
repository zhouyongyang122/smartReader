package com.qudiandu.smartreader.ui.task.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.event.SREventSelectedTask;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.main.model.bean.SRTaskTitle;
import com.qudiandu.smartreader.ui.task.model.SRTaskManager;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskCate;
import com.qudiandu.smartreader.ui.task.contract.SRTaskCateContract;
import com.qudiandu.smartreader.ui.task.model.SRTaskCateModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/23.
 */

public class SRTaskCatePresenter extends ZYListDataPresenter<SRTaskCateContract.IView, SRTaskCateModel, Object> implements SRTaskCateContract.IPresenter {

    String mBookId;

    public SRTaskCatePresenter(SRTaskCateContract.IView view, String bookId) {
        super(view, new SRTaskCateModel());
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
                        lastUnit = ((SRTaskCate) results.get(0)).unit;
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
        SRTaskCate taskCate = null;
        for (Object object : mDataList) {
            if (object instanceof SRTaskCate) {
                SRTaskCate result = (SRTaskCate) object;
                if (result.isCheck) {
                    taskCate = result;
                    break;
                }
            }
        }
        if (taskCate != null) {
            mSubscriptions.add(ZYNetSubscription.subscription(mModel.addTask(SRTaskManager.getInstance().getCurrentTaskClassId() + "", taskCate.book_id + "", taskCate.catalogue_id + ""), new ZYNetSubscriber() {
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
