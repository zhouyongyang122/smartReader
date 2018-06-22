package com.qudiandu.smartdub.ui.main.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.qudiandu.smartdub.ZYPreferenceHelper;
import com.qudiandu.smartdub.base.bean.ZYResponse;
import com.qudiandu.smartdub.base.event.SREventIdentityChange;
import com.qudiandu.smartdub.base.event.SREventJoinClassSuc;
import com.qudiandu.smartdub.base.event.SREventLogin;
import com.qudiandu.smartdub.base.event.SREventSelectedTask;
import com.qudiandu.smartdub.base.event.SREventUpdateClassName;
import com.qudiandu.smartdub.base.mvp.ZYBasePresenter;
import com.qudiandu.smartdub.service.net.ZYNetSubscriber;
import com.qudiandu.smartdub.service.net.ZYNetSubscription;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.login.model.bean.SRUser;
import com.qudiandu.smartdub.ui.main.contract.SRClassContract;
import com.qudiandu.smartdub.ui.main.model.SRMainModel;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;
import com.qudiandu.smartdub.ui.main.model.bean.SRBookJson;
import com.qudiandu.smartdub.ui.main.model.bean.SRCatalogue;
import com.qudiandu.smartdub.ui.main.model.bean.SRClass;
import com.qudiandu.smartdub.ui.main.model.bean.SRPage;
import com.qudiandu.smartdub.ui.main.model.bean.SRTask;
import com.qudiandu.smartdub.ui.main.model.bean.SRTaskTitle;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.ui.profile.model.SREditModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ZY on 17/7/22.
 */

public class SRClassPresenter extends ZYBasePresenter implements SRClassContract.IPresenter {

    SRClassContract.IView mView;

    List<SRClass> mClasses = new ArrayList<SRClass>();

    SRClass mCurrentClass;

    List<Object> mData = new ArrayList<Object>();

    int mCurrentClassPosition;

    SRMainModel mModel;

    boolean isFirstLoad = true;

    int mStart;

    int mRows = 20;

    boolean isRefreshing;

    SRTask mDelTask;

    public SRClassPresenter(SRClassContract.IView iView) {
        mModel = new SRMainModel();
        mView = iView;
        mView.setPresenter(this);
        EventBus.getDefault().register(this);
        isRefreshing = true;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        isFirstLoad = true;
        mCurrentClass = null;
        mData.clear();
        mClasses.clear();
        mView.showLoading();
        refreshClasss();
    }

    public void refreshClasss() {
        isRefreshing = true;
        SRUser user = SRUserManager.getInstance().getUser();
        Observable<ZYResponse<List<SRClass>>> observable = null;
        if (user.isNoIdentity()) {
            isRefreshing = false;
            return;
        } else if (user.isStudent()) {
            observable = mModel.getStudentClasss();
        } else {
            observable = mModel.getTeacherClasss();
        }
        mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<List<SRClass>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRClass>> response) {
                isRefreshing = false;
                if (response.data != null && response.data.size() > 0) {
                    mClasses.clear();
                    mClasses.addAll(response.data);
                    if (mCurrentClass == null) {
                        mCurrentClassPosition = 0;
                        mCurrentClass = mClasses.get(0);
                        ZYPreferenceHelper.getInstance().saveClassId(mCurrentClass.group_id + "");
                    } else {
                        setSelectClassPositionByClass();
                    }
                    mView.refreshClasses();
                    loadTasks(true);
                } else {
                    mView.showClassEmpty();
                }
            }

            @Override
            public void onFail(String message) {
                isRefreshing = false;
                if (isFirstLoad) {
                    mView.showError();
                }
            }
        }));
    }

    public void loadTasks(boolean isRefresh) {
        if (isRefresh) {
            mStart = 0;
        }
        isRefreshing = true;
        SRUser user = SRUserManager.getInstance().getUser();
        Observable<ZYResponse<List<SRTask>>> observable = null;
        if (user.isNoIdentity()) {
            isRefreshing = false;
            return;
        } else if (user.isStudent()) {
            observable = mModel.getStudentTasks(mCurrentClass.group_id, mStart, mRows);
        } else {
            observable = mModel.getTeacherTasks(mCurrentClass.group_id, mStart, mRows);
        }
        mSubscriptions.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<List<SRTask>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRTask>> response) {
                isFirstLoad = false;
                List<SRTask> resuts = response.data;
                if (mStart == 0) {
                    mData.clear();
                }
                if (resuts.size() > 0) {
                    mStart += resuts.size();

                    String lastTime = null;
                    if (mData.size() > 0) {
                        lastTime = ((SRTask) mData.get(mData.size() - 1)).getCreateTime();
                    } else {
                        lastTime = resuts.get(0).getCreateTime();
                        mData.add(new SRTaskTitle(lastTime));
                    }
                    for (SRTask task : resuts) {
                        if (lastTime.equals(task.getCreateTime())) {
                            mData.add(task);
                        } else {
                            lastTime = task.getCreateTime();
                            mData.add(new SRTaskTitle(lastTime));
                            mData.add(task);
                        }
                    }
                    mView.showList(true);
                } else {
                    if (mData.size() > 0) {
                        mView.showList(false);
                    } else {
                        mView.showClassTaskEmpty();
                    }
                }
                isRefreshing = false;
            }

            @Override
            public void onFail(String message) {
                if (isFirstLoad) {
                    mView.showError();
                } else {
                    super.onFail(message);
                }
                isRefreshing = false;
            }
        }));
    }

    public void updateIdentity(final int indentity, String code) {
        mView.showProgress();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_type", indentity + "");
        if (!TextUtils.isEmpty(code)) {
            params.put("code", code);
        }
        mSubscriptions.add(ZYNetSubscription.subscription(new SREditModel().editUser(params), new ZYNetSubscriber<ZYResponse<SRUser>>() {
            @Override
            public void onSuccess(ZYResponse<SRUser> response) {
                mView.hideProgress();
                SRUserManager.getInstance().setUser(response.data);
                mView.choseIdentitySuc();
            }

            @Override
            public void onFail(String message) {
                mView.hideProgress();
                super.onFail(message);
            }
        }));
    }

    void setSelectClassPositionByClass() {
        mCurrentClassPosition = 0;
        int position = 0;
        for (SRClass value : mClasses) {
            if (value.group_id == mCurrentClass.group_id) {
                mCurrentClassPosition = position;
                break;
            }
            position++;
        }
    }

    public void toFinishTask(final String bookLocalPath, final SRTask task) {
        mView.showProgress();
        Observable<SRBook> observable = Observable.create(new Observable.OnSubscribe<SRBook>() {
            @Override
            public void call(Subscriber<? super SRBook> subscriber) {
                try {
                    FileInputStream inputStream = new FileInputStream(new File(bookLocalPath + "book.json"));
                    JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                    Gson gson = new Gson();
                    SRBookJson bookJson = gson.fromJson(reader, SRBookJson.class);
                    SRBook book = bookJson.book;
                    //设置tract的音频路径和track对应的mark的id....
                    for (SRPage page : book.getPage()) {
                        for (SRTract tract : page.getTrack()) {
                            tract.setMp3Path(bookLocalPath + "mp3/" + tract.getMp3name());
                            tract.setBook_id(book.getBook_id_int());
                            tract.setMarkId(book.getId() + "_" + page.getPage_id() + "_" + tract.getTrack_id());
                        }

                        //设置page对应的单元小节id
                        for (SRCatalogue catalogue : book.catalogue) {
                            if (catalogue.containsPage(page.getPage_id() + "")) {
                                page.setCatalogueId(catalogue.getCatalogue_id());
                                break;
                            }
                        }
                    }
                    subscriber.onNext(book);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
        Subscriber subscriber = new Subscriber<SRBook>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgress();
                mView.showToast("加载任务相关的课本失败,请重新尝试,或者删除相关课本重新下载!");
            }

            @Override
            public void onNext(SRBook bookData) {
                mView.hideProgress();
                ArrayList<SRTract> tracts = new ArrayList<SRTract>();
                for (SRPage page : bookData.getPage()) {
                    if (page.getCatalogueId() == task.catalogue_id) {
                        tracts.addAll(page.getTrack());
                    } else if (tracts.size() > 0) {
                        break;
                    }
                }
                mView.toFinishTask(task, tracts);
            }
        };
        mSubscriptions.add(ZYNetSubscription.subscription(observable, subscriber));
    }

    public List<SRClass> getClasses() {
        return mClasses;
    }

    public void setSelectClass(int position) {
        mCurrentClass = mClasses.get(position);
        ZYPreferenceHelper.getInstance().saveClassId(mCurrentClass.group_id + "");
        mView.showLoading();
        loadTasks(true);
    }

    public void delTask() {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.delTask(mDelTask.task_id + ""), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
                super.onSuccess(response);
                mView.hideProgress();
                getData().remove(mDelTask);
                boolean isTitleVH = false;
                Iterator<Object> dataIterator = getData().iterator();
                while (dataIterator.hasNext()) {
                    Object object = dataIterator.next();
                    if (object instanceof SRTaskTitle) {
                        if (isTitleVH) {
                            dataIterator.remove();
                            break;
                        }
                        isTitleVH = true;
                    } else {
                        isTitleVH = false;
                    }
                }
                mView.delTaskSuc();
            }

            @Override
            public void onFail(String message) {
                mView.hideProgress();
                super.onFail(message);
            }
        }));
    }

    public void setDelTask(SRTask delTask) {
        mDelTask = delTask;
    }

    public int getCurrentClassPosition() {
        return mCurrentClassPosition;
    }

    public List<String> getClassNames() {
        List<String> names = new ArrayList<String>();
        for (SRClass srClass : mClasses) {
            names.add(srClass.class_name);
        }
        return names;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SREventIdentityChange identityChange) {
        mCurrentClass = null;
        subscribe();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SREventUpdateClassName mClass) {
        if (mCurrentClass != null) {
            mCurrentClass.class_name = mClass.mClass.class_name;
            mView.refreshClasses();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SREventJoinClassSuc joinClassSuc) {
        refreshClasss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SREventSelectedTask srEventSelectedTask) {
        refreshClasss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SREventLogin eventLogin) {
        subscribe();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        EventBus.getDefault().unregister(this);
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public List<Object> getData() {
        return mData;
    }

    public SRClass getCurrentClass() {
        return mCurrentClass;
    }
}
