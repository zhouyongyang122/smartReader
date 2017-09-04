package com.qudiandu.smartreader.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.gson.JsonObject;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYFragmentAdapter;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.base.view.ZYLoadingView;
import com.qudiandu.smartreader.base.view.ZYNoScollViewPager;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
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
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/9/3.
 */

public class SRTaskProblemActivity extends ZYBaseActivity implements View.OnClickListener {

    public static final String TASK_ID = "task_id";

    @Bind(R.id.viewPage)
    ZYNoScollViewPager mViewPage;

    ZYLoadingView mLoadingView;

    CompositeSubscription mSubscription;

    int mTaskId;

    SRTaskProblem mTaskProblem;

    Map<String, String> mAnswers = new HashMap<String, String>();

    List<SRTaskProblemPresenter> mPresenters = new ArrayList<SRTaskProblemPresenter>();

    int mPage;

    public static Intent createIntent(Context context, int taskId) {
        Intent intent = new Intent(context, SRTaskProblemActivity.class);
        intent.putExtra(TASK_ID, taskId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_task_problem);
        initLoadingView();
        mTaskId = getIntent().getIntExtra(TASK_ID, 0);
        mLoadingView.showLoading();
        loadData();
    }

    private void initLoadingView() {
        mLoadingView = new ZYLoadingView(this);
        mLoadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.showLoading();
                loadData();
            }
        });
    }

    private void initFragments() {
        ZYFragmentAdapter adapter = new ZYFragmentAdapter(this.getSupportFragmentManager());
        SRTaskProblemFragment problemFragment;
        for (SRTaskProblem.Problem problem : mTaskProblem.problems) {
            problemFragment = new SRTaskProblemFragment();
            mPresenters.add(new SRTaskProblemPresenter(problemFragment, problem, mTaskProblem.getTeacher()));
            adapter.addFragment(problemFragment, problem.title + "");
        }
        mViewPage.setAdapter(adapter);
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                SRPlayManager.getInstance().stopAudio();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPage.setOffscreenPageLimit(4);
    }

    private void loadData() {
        mSubscription.add(ZYNetSubscription.subscription(new SRTaskModel().getProblemTaskDetail(mTaskId + ""), new ZYNetSubscriber<ZYResponse<SRTaskProblem>>() {

            @Override
            public void onSuccess(ZYResponse<SRTaskProblem> response) {
                mLoadingView.showNothing();
                mTaskProblem = response.data;
                showTitle(mTaskProblem.title);
                showActionRightTitle("下一题", SRTaskProblemActivity.this);
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
        mPage++;
        if (mPage >= mTaskProblem.problems.size()) {
            showActionRightTitle("提交", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject(mAnswers);
                    showProgress();
                    mSubscription.add(ZYNetSubscription.subscription(new SRTaskModel().submitAnswer(mTaskId, mTaskProblem.group_id, jsonObject.toString()), new ZYNetSubscriber() {
                        @Override
                        public void onSuccess(ZYResponse response) {
                            hideProgress();
                            ZYToast.show(SRTaskProblemActivity.this, "任务提交成功!");
                        }

                        @Override
                        public void onFail(String message) {
                            hideProgress();
                            super.onFail(message);
                        }
                    }));
                }
            });
            return;
        }
        if (mPresenters.get(mPage).isFinised()) {
            mViewPage.setCurrentItem(mPage);
        } else {
            ZYToast.show(SRTaskProblemActivity.this, "还没有完成当前的任务哦!");
        }
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
