package com.qudiandu.smartreader.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.task.model.SRTaskModel;

import butterknife.Bind;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskCommentActivity extends ZYBaseActivity {

    static final String SHOW_ID = "showId";

    public static Intent createIntent(Context context, String showId) {
        Intent intent = new Intent(context, SRTaskCommentActivity.class);
        intent.putExtra(SHOW_ID, showId);
        return intent;
    }

    @Bind(R.id.textMsg)
    EditText textMsg;

    CompositeSubscription subscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_task_comment);

        mActionBar.showTitle("评论");
        textMsg.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});

        showActionRightTitle("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comment = textMsg.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    showToast("评论内容不能为空");
                    return;
                }
                showProgress();
                subscription.add(ZYNetSubscription.subscription(new SRTaskModel().addComment(getIntent().getStringExtra(SHOW_ID), comment), new ZYNetSubscriber() {
                    @Override
                    public void onSuccess(ZYResponse response) {
                        hideProgress();
                        showToast("评论成功!");
                        Intent intent = new Intent();
                        intent.putExtra("comment", comment);
                        setResult(100, intent);
                        finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
