package com.qudiandu.smartreader.ui.main.contract;

import com.qudiandu.smartreader.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYIBaseView;
import com.qudiandu.smartreader.base.view.ZYIListView;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/22.
 */

public interface SRClassContract {

    interface IView extends ZYIBaseView<IPresenter>, ZYIListView {
        void choseIdentitySuc();
        void refreshClasses();
        void showClassEmpty();
        void showClassTaskEmpty();
        void toFinishTask(SRTask task, ArrayList<SRTract> tracts);
    }

    interface IPresenter extends ZYIBasePresenter {
        void setSelectClass(int position);
        List<SRClass> getClasses();
        int getCurrentClassPosition();
        void refreshClasss();
        List<Object> getData();
        void loadTasks(boolean isRefresh);
        void updateIdentity(int indentity);
        boolean isRefreshing();
        void toFinishTask(final String bookLocalPath, final SRTask task);
        SRClass getCurrentClass();
    }
}
