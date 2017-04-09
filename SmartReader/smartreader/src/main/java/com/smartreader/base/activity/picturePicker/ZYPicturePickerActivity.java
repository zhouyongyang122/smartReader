package com.smartreader.base.activity.picturePicker;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.smartreader.R;
import com.smartreader.base.mvp.ZYBaseActivity;
import com.smartreader.utils.ZYStatusBarUtils;

public class ZYPicturePickerActivity extends ZYBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_base_fragment);
        hideActionBar();

        Bundle bundle = getIntent().getExtras();
        boolean isSingle = bundle.getBoolean(ZYPicker.KEY_IS_SINGLE, false);
        int maxSelect = bundle.getInt(ZYPicker.KEY_MAX_SELECT, 1);

        ZYPicturePickerFragment fragment = (ZYPicturePickerFragment) getSupportFragmentManager().findFragmentById(R.id.layoutContent);

        if (fragment == null) {
            fragment = new ZYPicturePickerFragment();
        }

        if (isSingle) {
            new ZYPicturePickerPresenter(fragment);
        } else {
            new ZYPicturePickerPresenter(fragment, maxSelect);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layoutContent, fragment);
        transaction.commit();
    }
}
