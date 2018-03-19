package com.qudiandu.smartreader.ui.task.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.activity.SRMainActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.task.model.SRTaskModel;
import com.qudiandu.smartreader.ui.task.presenter.SRTaskDetailPresenter;
import com.qudiandu.smartreader.ui.task.view.SRTaskDetailFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskDetailActivity extends ZYBaseFragmentActivity<SRTaskDetailFragment> {

    static final String TASK = "task";

    public static Intent createIntent(Context context, SRTask task) {
        Intent intent = new Intent(context, SRTaskDetailActivity.class);
        intent.putExtra(TASK, task);
        return intent;
    }

    SRTask mTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("任务详情");
        showActionRightTitle("任务提醒", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SRTaskDetailActivity.this).setTitle("任务提醒").setMessage("快快快,完成练习哦!")
                        .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ZYNetSubscription.subscription(new SRTaskModel().taskRemind(mTask.group_id + "", mTask.task_id + ""), new ZYNetSubscriber() {
                                    @Override
                                    public void onSuccess(ZYResponse response) {
                                        showToast("成功任务提醒");
                                    }

                                    @Override
                                    public void onFail(String message) {
                                        super.onFail(message);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
        });

        mTask = (SRTask) getIntent().getSerializableExtra(TASK);
        new SRTaskDetailPresenter(mFragment, mTask);
    }

    @Override
    protected SRTaskDetailFragment createFragment() {
        return new SRTaskDetailFragment();
    }
}
