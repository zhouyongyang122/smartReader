package com.qudiandu.smartreader.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYFragmentAdapter;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.base.player.ZYAudioPlayManager;
import com.qudiandu.smartreader.base.view.ZYLoadingView;
import com.qudiandu.smartreader.base.view.ZYNoScollViewPager;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.task.model.SRTaskModel;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;
import com.qudiandu.smartreader.ui.task.view.SRTaskListenFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/9/9.
 */

public class SRTaskListenActivity extends ZYBaseActivity {

    public static final String TASK_ID = "task_id";

    public static final String FINISH_TASK = "SRTaskFinish";

    @Bind(R.id.viewPage)
    ZYNoScollViewPager mViewPage;

    Map<String, String> mAnswers = new HashMap<String, String>();

    int mTaskId;

    SRTaskFinish mTaskFinish;

    ZYLoadingView mLoadingView;

    CompositeSubscription mSubscription;

    ZYFragmentAdapter mAdapter;

    SRTaskProblem mTaskProblem;

    int mCurPage;

    public static Intent createIntent(Context context, int taskId) {
        Intent intent = new Intent(context, SRTaskListenActivity.class);
        intent.putExtra(TASK_ID, taskId);
        return intent;
    }

    public static Intent createIntent(Context context, SRTaskFinish taskFinish) {
        Intent intent = new Intent(context, SRTaskListenActivity.class);
        intent.putExtra(FINISH_TASK, taskFinish);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_task_problem);
        initLoadingView();
        mSubscription = new CompositeSubscription();
        mTaskId = getIntent().getIntExtra(TASK_ID, 0);
        mTaskFinish = (SRTaskFinish) getIntent().getSerializableExtra(FINISH_TASK);
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

    private void loadData() {

        Observable<ZYResponse<SRTaskProblem>> observable = null;
        if (mTaskFinish != null) {
            observable = new SRTaskModel().getProblemFinishTaskDetail(mTaskFinish.finish_id + "");
        } else {
            observable = new SRTaskModel().getProblemTaskDetail(mTaskId + "");
        }

        mSubscription.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<SRTaskProblem>>() {
            @Override
            public void onSuccess(ZYResponse<SRTaskProblem> response) {
                mLoadingView.showNothing();
                initFragments(response.data);
            }

            @Override
            public void onFail(String message) {
                mLoadingView.showError();
            }
        }));
    }

    private void initFragments(SRTaskProblem taskProblem) {
        mTaskProblem = taskProblem;
        mAdapter = new ZYFragmentAdapter(this.getSupportFragmentManager());
        SRTaskListenFragment problemFragment = null;
        SRTaskProblem.Problem problem = null;
        for (int i = 0; i < taskProblem.problems.size(); i++) {
            problem = taskProblem.problems.get(i);
            problemFragment = new SRTaskListenFragment();
            problemFragment.init(taskProblem, problem, i > 0, i < taskProblem.problems.size() - 1, mTaskFinish);
            mAdapter.addFragment(problemFragment, problem.title + "");
        }
        mViewPage.setAdapter(mAdapter);
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SRTaskListenFragment listenFragment = (SRTaskListenFragment) mAdapter.getItem(mCurPage);
                addAnswer(listenFragment.getProblem().problem_id + "", listenFragment.getListenTime() / 1000 + "");
                mCurPage = position;
                ((SRTaskListenFragment) mAdapter.getItem(position)).play();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPage.setOffscreenPageLimit(4);
        ((SRTaskListenFragment) mAdapter.getItem(0)).play();
    }

    public void addAnswer(String problemId, String answer) {
        mAnswers.put(problemId, answer);
    }

    public void next() {
        mViewPage.setCurrentItem(mCurPage + 1);
    }

    public void pre() {
        mViewPage.setCurrentItem(mCurPage - 1);
    }

    @Override
    public void onBackPressed() {

        try {
            if (!SRUserManager.getInstance().getUser().isTeacher()) {
                SRTaskListenFragment listenFragment = (SRTaskListenFragment) mAdapter.getItem(mCurPage);
                addAnswer(listenFragment.getProblem().problem_id + "", listenFragment.getListenTime() / 1000 + "");
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String value = gson.toJson(mAnswers);
                ZYNetSubscription.subscription(new SRTaskModel().submitAnswer(mTaskId, mTaskProblem.group_id, value), new ZYNetSubscriber() {
                    @Override
                    public void onSuccess(ZYResponse response) {
                    }

                    @Override
                    public void onFail(String message) {
                    }
                });
            }
        } catch (Exception e) {

        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        ZYAudioPlayManager.getInstance().stop();
    }
}
