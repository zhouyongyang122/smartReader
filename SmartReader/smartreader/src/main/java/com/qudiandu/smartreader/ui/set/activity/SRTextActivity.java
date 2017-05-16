package com.qudiandu.smartreader.ui.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/7.
 */

public class SRTextActivity extends ZYBaseActivity {



    @Bind(R.id.textMsg)
    TextView textMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_text);
    }
}
