package com.qudiandu.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.base.view.ZYWheelSelectDialog;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.model.SRMainModel;
import com.qudiandu.smartreader.utils.ZYToast;

import butterknife.Bind;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/7/23.
 */

public class SRCreateClassActivity extends ZYBaseActivity {

    @Bind(R.id.textSchool)
    EditText textSchool;

    @Bind(R.id.textGrade)
    TextView textGrade;

    @Bind(R.id.textClass)
    EditText textClass;

    @Bind(R.id.textPhone)
    EditText textPhone;

    ZYWheelSelectDialog wheelSelectDialog;

    int greade = 0;

    public static Intent createIntent(Context context) {
        return new Intent(context, SRCreateClassActivity.class);
    }

    CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_create_class);

        showTitle("创建班级");
        showActionRightTitle("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSchoolValue = textSchool.getText().toString();
                String textGradeValue = textGrade.getText().toString();
                String textClassValue = textClass.getText().toString();
                String textPhoneValue = textPhone.getText().toString();
                if (TextUtils.isEmpty(textSchoolValue)) {
                    ZYToast.show(SRCreateClassActivity.this, "学校不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(textGradeValue)) {
                    ZYToast.show(SRCreateClassActivity.this, "年级不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(textClassValue)) {
                    ZYToast.show(SRCreateClassActivity.this, "班级名称不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(textPhoneValue)) {
                    ZYToast.show(SRCreateClassActivity.this, "手机号码不能为空!");
                    return;
                }
                showProgress();
                mSubscription.add(ZYNetSubscription.subscription(new SRMainModel().addClass(textSchoolValue, greade + "", textClassValue, textPhoneValue), new ZYNetSubscriber() {
                    @Override
                    public void onSuccess(ZYResponse response) {
                        hideProgress();
                        ZYToast.show(SRCreateClassActivity.this, "班级申请成功,请等待审核结果!");
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

    @OnClick({R.id.textGrade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textGrade:
                if (wheelSelectDialog == null) {
                    wheelSelectDialog = new ZYWheelSelectDialog(this, new String[]{"一年级", "二年级", "三年级", "四年级", "五年级", "六年级", "七年级", "八年级", "九年级"}, new ZYWheelSelectDialog.WheelSelectListener() {
                        @Override
                        public void onWheelSelected(ZYWheelSelectDialog dialog, int position, String value) {
                            textGrade.setText(value);
                            greade = position + 1;
                        }
                    });
                }
                wheelSelectDialog.showDialog(greade <= 0 ? 0 : (greade - 1));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }
}
