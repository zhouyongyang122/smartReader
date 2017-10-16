package com.qudiandu.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.presenter.SRClassDetailPresenter;
import com.qudiandu.smartreader.ui.main.view.SRClassDetailFragment;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailActivity extends ZYBaseFragmentActivity<SRClassDetailFragment> {

    static final String CLASS = "class";

    SRClass mClass;

    public static Intent createIntent(Context context, SRClass srClass) {
        Intent intent = new Intent(context, SRClassDetailActivity.class);
        intent.putExtra(CLASS, srClass);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClass = (SRClass) getIntent().getSerializableExtra(CLASS);
        showTitle("班级成员(" + mClass.cur_num + ")");
        if (SRUserManager.getInstance().getUser().uid.equals(mClass.uid + "")) {
            showActionRightTitle("管理", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(SRClassUsersActivity.createIntent(SRClassDetailActivity.this, mClass.group_id + ""));
                }
            });
        }
        new SRClassDetailPresenter(mFragment, mClass.group_id + "");
    }

    @Override
    protected SRClassDetailFragment createFragment() {
        return new SRClassDetailFragment();
    }
}
