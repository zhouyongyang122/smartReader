package com.qudiandu.smartdub.ui.main.contract;

import com.qudiandu.smartdub.base.mvp.ZYIBasePresenter;
import com.qudiandu.smartdub.base.mvp.ZYIBaseView;
import com.qudiandu.smartdub.base.view.ZYIListView;
import com.qudiandu.smartdub.ui.main.model.bean.SRClass;
import com.qudiandu.smartdub.ui.main.model.bean.SRTask;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;

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

        void delTaskSuc();
    }

    interface IPresenter extends ZYIBasePresenter {
        void setSelectClass(int position);

        List<SRClass> getClasses();

        int getCurrentClassPosition();

        void refreshClasss();

        List<String> getClassNames();

        List<Object> getData();

        void loadTasks(boolean isRefresh);

        void updateIdentity(int indentity, String code);

        boolean isRefreshing();

        void toFinishTask(final String bookLocalPath, final SRTask task);

        SRClass getCurrentClass();

        void delTask();

        void setDelTask(SRTask delTask);
    }
}
