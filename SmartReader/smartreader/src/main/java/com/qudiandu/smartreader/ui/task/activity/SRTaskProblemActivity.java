package com.qudiandu.smartreader.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYFragmentAdapter;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.base.view.ZYLoadingView;
import com.qudiandu.smartreader.base.view.ZYNoScollViewPager;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.task.model.SRTaskModel;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;
import com.qudiandu.smartreader.ui.task.presenter.SRTaskProblemPresenter;
import com.qudiandu.smartreader.ui.task.view.SRTaskProblemFragment;
import com.qudiandu.smartreader.utils.ZYToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/9/3.
 */

public class SRTaskProblemActivity extends ZYBaseActivity implements View.OnClickListener {

    public static final String TASK_ID = "task_id";

    public static final String FINISH_ID = "finish_id";

    @Bind(R.id.viewPage)
    ZYNoScollViewPager mViewPage;

    ZYLoadingView mLoadingView;

    CompositeSubscription mSubscription;

    int mTaskId;

    int mFinisId;

    SRTaskProblem mTaskProblem;

    Map<String, String> mAnswers = new HashMap<String, String>();

    List<SRTaskProblemPresenter> mPresenters = new ArrayList<SRTaskProblemPresenter>();

    ZYFragmentAdapter mAdapter;

    int mPage;

    public static Intent createIntent(Context context, int taskId, int finisId) {
        Intent intent = new Intent(context, SRTaskProblemActivity.class);
        intent.putExtra(TASK_ID, taskId);
        intent.putExtra(FINISH_ID, finisId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_task_problem);
        mSubscription = new CompositeSubscription();
        initLoadingView();
        mTaskId = getIntent().getIntExtra(TASK_ID, 0);
        mFinisId = getIntent().getIntExtra(FINISH_ID, 0);
        mLoadingView.showLoading();
        loadData();
    }

    private void initLoadingView() {
        mLoadingView = new ZYLoadingView(this);
        mLoadingView.attach(mRootView);
        mLoadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.showLoading();
                loadData();
            }
        });
    }

    private void initFragments() {
        mAdapter = new ZYFragmentAdapter(this.getSupportFragmentManager());
        SRTaskProblemFragment problemFragment;
        for (SRTaskProblem.Problem problem : mTaskProblem.problems) {
            problemFragment = new SRTaskProblemFragment();
            mPresenters.add(new SRTaskProblemPresenter(problemFragment, problem, mTaskProblem.getTeacher()));
            mAdapter.addFragment(problemFragment, problem.title + "");
        }
        mViewPage.setAdapter(mAdapter);
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                SRPlayManager.getInstance().stopAudio();
            }

            @Override
            public void onPageSelected(int position) {
                ((SRTaskProblemFragment) mAdapter.getItem(position)).play();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPage.setOffscreenPageLimit(4);

        ((SRTaskProblemFragment) mAdapter.getItem(0)).play();
    }

    private void loadData() {
        Observable<ZYResponse<SRTaskProblem>> observable = null;
        if (mFinisId > 0) {
            observable = new SRTaskModel().getProblemFinishTaskDetail(mFinisId + "");
        } else {
            observable = new SRTaskModel().getProblemTaskDetail(mTaskId + "");
        }
        mSubscription.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<SRTaskProblem>>() {

            @Override
            public void onSuccess(ZYResponse<SRTaskProblem> response) {
                mLoadingView.showNothing();
                mTaskProblem = response.data;
                showTitle(mTaskProblem.title);
                if (mTaskProblem.problems.size() > 1) {
                    showActionRightTitle("下一题", SRTaskProblemActivity.this);
                } else {
                    submit();
                }
                initFragments();
            }

            @Override
            public void onFail(String message) {
                mLoadingView.showError();
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (SRUserManager.getInstance().getUser().isStudent() && !mPresenters.get(mPage).isFinised()) {
            ZYToast.show(SRTaskProblemActivity.this, "还没有完成当前的任务哦!");
            return;
        }
        mPage++;
        mViewPage.setCurrentItem(mPage);
        if (mPage >= mTaskProblem.problems.size() - 1) {
            submit();
        }
    }

    private void submit() {

        String title = "提交";
        if (SRUserManager.getInstance().getUser().isTeacher()) {
            title = "完成";
        }
        showActionRightTitle(title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SRUserManager.getInstance().getUser().isTeacher()) {
                    onBackPressed();
                    return;
                }

                if (!mPresenters.get(mPage).isFinised()) {
                    ZYToast.show(SRTaskProblemActivity.this, "还没有完成当前的任务哦!");
                    return;
                }

                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String value = gson.toJson(mAnswers);

                showProgress();
                mSubscription.add(ZYNetSubscription.subscription(new SRTaskModel().submitAnswer(mTaskId, mTaskProblem.group_id, value), new ZYNetSubscriber() {
                    @Override
                    public void onSuccess(ZYResponse response) {
                        hideProgress();
                        ZYToast.show(SRTaskProblemActivity.this, "任务提交成功!");
                        onBackPressed();
                    }

                    @Override
                    public void onFail(String message) {
                        hideProgress();
                        super.onFail(message);
                    }
                }));
            }
        });
    }

    public void addAnswer(String problemId, String answer) {
        mAnswers.put(problemId, answer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        SRPlayManager.getInstance().stopAudio();
    }
}
