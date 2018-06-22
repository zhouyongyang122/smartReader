package com.qudiandu.smartdub.ui.profile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartdub.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartdub.ui.profile.presenter.SREditPresenter;
import com.qudiandu.smartdub.ui.profile.view.SREditFragment;

/**
 * Created by ZY on 17/4/7.
 */

public class SREditActivity extends ZYBaseFragmentActivity<SREditFragment> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.showTitle("我的信息");
        new SREditPresenter(mFragment);

//        showActionRightTitle("保存", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mFragment.save();
//            }
//        });
    }

    @Override
    protected SREditFragment createFragment() {
        return new SREditFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment.onActivityResult(requestCode, resultCode, data);
    }
}
