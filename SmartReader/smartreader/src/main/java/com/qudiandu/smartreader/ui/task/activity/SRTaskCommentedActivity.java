package com.qudiandu.smartreader.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.utils.ZYDateUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/31.
 */

public class SRTaskCommentedActivity extends ZYBaseActivity {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textSubTitle)
    TextView textSubTitle;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.textScore)
    TextView textScore;

    @Bind(R.id.textComment)
    TextView textComment;

    SRTask mData;

    public static Intent createIntent(Context context, SRTask task) {
        Intent intent = new Intent(context, SRTaskCommentedActivity.class);
        intent.putExtra("task", task);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_task_commented);
        mData = (SRTask) getIntent().getSerializableExtra("task");
        ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, mData.page_url);
        textTitle.setText(mData.unit);
        textSubTitle.setText(mData.title);
        SRTask.Finish finish = mData.finish.get(0);
        textScore.setText(finish.score + "");
        textComment.setText(finish.comment);
        textTime.setText("完成时间 " + ZYDateUtils.getTimeString(Long.parseLong(mData.create_time) * 1000, ZYDateUtils.MMDDHHMM12));
    }
}
