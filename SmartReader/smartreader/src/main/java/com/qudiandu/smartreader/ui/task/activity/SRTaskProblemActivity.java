package com.qudiandu.smartreader.ui.task.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.qudiandu.smartreader.ui.task.presenter.SRTaskProblemPresenter;
import com.qudiandu.smartreader.ui.task.view.SRTaskProblemFragment;
import com.qudiandu.smartreader.utils.ZYToast;

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

public class SRTaskProblemActivity extends ZYBaseActivity {

    public static final String TASK_ID = "task_id";

    public static final String FINISH_TASK = "SRTaskFinish";

    @Bind(R.id.viewPage)
    ZYNoScollViewPager mViewPage;

    ZYLoadingView mLoadingView;

    CompositeSubscription mSubscription;

    int mTaskId;

    SRTaskFinish mTaskFinish;

    SRTaskProblem mTaskProblem;

    Map<String, String> mAnswers = new HashMap<String, String>();

    List<SRTaskProblemPresenter> mPresenters = new ArrayList<SRTaskProblemPresenter>();

    ZYFragmentAdapter mAdapter;

    int mCurPage;

    public static Intent createIntent(Context context, int taskId, int finisId) {
        Intent intent = new Intent(context, SRTaskProblemActivity.class);
        intent.putExtra(TASK_ID, taskId);
        return intent;
    }

    public static Intent createIntent(Context context, SRTaskFinish taskFinish) {
        Intent intent = new Intent(context, SRTaskProblemActivity.class);
        intent.putExtra(FINISH_TASK, taskFinish);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_task_problem);
        mSubscription = new CompositeSubscription();
        initLoadingView();
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

    private void initFragments() {
        mAdapter = new ZYFragmentAdapter(this.getSupportFragmentManager());
        SRTaskProblemFragment problemFragment;
        int index = 0;
        for (SRTaskProblem.Problem problem : mTaskProblem.problems) {
            problemFragment = new SRTaskProblemFragment();
            problemFragment.init(index > 0, index < mTaskProblem.problems.size() - 1, mTaskFinish);
            mPresenters.add(new SRTaskProblemPresenter(problemFragment, problem, mTaskProblem.getTeacher()));
            mAdapter.addFragment(problemFragment, problem.title + "");
            index++;
        }
        mViewPage.setAdapter(mAdapter);
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurPage = position;
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
        if (mTaskFinish != null) {
            observable = new SRTaskModel().getProblemFinishTaskDetail(mTaskFinish.finish_id + "");
        } else {
            observable = new SRTaskModel().getProblemTaskDetail(mTaskId + "");
        }
        mSubscription.add(ZYNetSubscription.subscription(observable, new ZYNetSubscriber<ZYResponse<SRTaskProblem>>() {

            @Override
            public void onSuccess(ZYResponse<SRTaskProblem> response) {
                mLoadingView.showNothing();
                mTaskProblem = response.data;
                showTitle(mTaskProblem.title);
                initFragments();
            }

            @Override
            public void onFail(String message) {
                mLoadingView.showError();
            }
        }));
    }

    public void addAnswer(String problemId, String answer) {
        mAnswers.put(problemId, answer);
    }

    @Override
    public void onBackPressed() {
        if (!SRUserManager.getInstance().getUser().isTeacher()) {
            new AlertDialog.Builder(this).setTitle("放弃").setMessage("任务未完成，是否放弃？")
                    .setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        } else {
            super.onBackPressed();
        }
    }

    public void next() {
        mViewPage.setCurrentItem(mCurPage + 1);
    }

    public void pre() {
        mViewPage.setCurrentItem(mCurPage - 1);
    }

    public void submit() {
        if (SRUserManager.getInstance().getUser().isTeacher()) {
            onBackPressed();
            return;
        }

        new AlertDialog.Builder(this).setTitle("提交").setMessage("亲,确定所有题目回答完毕,并确定答案准备提交吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        String value = gson.toJson(mAnswers);

                        showProgress();
                        mSubscription.add(ZYNetSubscription.subscription(new SRTaskModel().submitAnswer(mTaskId, mTaskProblem.group_id, value), new ZYNetSubscriber() {
                            @Override
                            public void onSuccess(ZYResponse response) {
                                hideProgress();
                                ZYToast.show(SRTaskProblemActivity.this, "任务提交成功!");
                                finish();
                            }

                            @Override
                            public void onFail(String message) {
                                hideProgress();
                                super.onFail(message);
                            }
                        }));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
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
