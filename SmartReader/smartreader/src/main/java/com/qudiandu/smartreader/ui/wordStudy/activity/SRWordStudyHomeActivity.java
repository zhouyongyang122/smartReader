package com.qudiandu.smartreader.ui.wordStudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.wordStudy.model.SRWordStudyModel;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyUnit;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyWord;
import com.qudiandu.smartreader.ui.wordStudy.view.viewHolder.SRWordStudyHomeMenuVH;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyHomeActivity extends ZYBaseActivity {

    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textWordNum)
    TextView textWordNum;

    SRWordStudyHomeMenuVH homeMenuVH;

    ArrayList<SRWordStudyWord> mData = new ArrayList<SRWordStudyWord>();

    public static Intent createIntent(Context context, SRWordStudyUnit unit) {
        Intent intent = new Intent(context, SRWordStudyHomeActivity.class);
        intent.putExtra("unit", unit);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_word_study_home);
        ButterKnife.bind(this);
        showWaitDialog("");
        SRWordStudyUnit unit = (SRWordStudyUnit) getIntent().getSerializableExtra("unit");
        textName.setText(unit.unit);
        mSubscriptions.add(ZYNetSubscription.subscription(new SRWordStudyModel().getUnitWords(unit.unit_id + ""), new ZYNetSubscriber<ZYResponse<List<SRWordStudyWord>>>() {
            @Override
            public void onSuccess(ZYResponse<List<SRWordStudyWord>> response) {
                super.onSuccess(response);
                mData.addAll(response.data);
                textWordNum.setText("共" + mData.size() + "个词汇");
                homeMenuVH.updateView(mData, 0);
                hideWaitDialog();
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                hideWaitDialog();
                finish();
            }
        }));

        mActionBar.showActionRightImg(R.drawable.menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeMenuVH.show();
            }
        });

        homeMenuVH = new SRWordStudyHomeMenuVH();
        homeMenuVH.attachTo(mRootView);
        homeMenuVH.hide();
    }

    @OnClick({R.id.layoutPractice, R.id.layoutListen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutPractice:
                startActivity(SRWordStudyPracticeActivity.createIntent(this, mData));
                break;
            case R.id.layoutListen:
                startActivity(SRWordStudyListenActivity.createIntent(this, mData));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (homeMenuVH.isvisiable()) {
            homeMenuVH.hide();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
