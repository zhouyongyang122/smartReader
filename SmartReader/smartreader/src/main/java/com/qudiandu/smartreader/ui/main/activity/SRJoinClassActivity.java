package com.qudiandu.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.ZYPreferenceHelper;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.event.SREventJoinClassSuc;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;
import com.qudiandu.smartreader.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/7/23.
 */

public class SRJoinClassActivity extends ZYBaseActivity {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textCode)
    TextView textCode;

    public static Intent createIntent(Context context) {
        return new Intent(context, SRJoinClassActivity.class);
    }

    CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_join_class);

        showTitle("加入班级");
        showActionRightTitle("加入班级", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textNameValue = textName.getText().toString();
                String textCodeValue = textCode.getText().toString();
                if (TextUtils.isEmpty(textNameValue)) {
                    ZYToast.show(SRJoinClassActivity.this, "真实姓名不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(textCodeValue)) {
                    ZYToast.show(SRJoinClassActivity.this, "班级邀请码不能为空!");
                    return;
                }
                showProgress();
                mSubscription.add(ZYNetSubscription.subscription(new SRMainModel().joinClass("", textNameValue, textCodeValue), new ZYNetSubscriber() {
                    @Override
                    public void onSuccess(ZYResponse response) {
                        hideProgress();
                        ZYToast.show(SRJoinClassActivity.this, "加入班级成功!");
                        EventBus.getDefault().post(new SREventJoinClassSuc());
                        ZYPreferenceHelper.getInstance().identityComfirm();
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
        mSubscription.unsubscribe();
    }
}
