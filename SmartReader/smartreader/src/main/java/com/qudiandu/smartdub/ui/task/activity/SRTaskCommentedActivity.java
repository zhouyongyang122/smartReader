package com.qudiandu.smartdub.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.event.ZYAudionPlayEvent;
import com.qudiandu.smartdub.base.mvp.ZYBaseActivity;
import com.qudiandu.smartdub.base.player.ZYAudioPlayManager;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.main.model.bean.SRTask;
import com.qudiandu.smartdub.utils.ZYDateUtils;
import com.qudiandu.smartdub.utils.ZYUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

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

    @Bind(R.id.layoutVoice)
    RelativeLayout layoutVoice;

    @Bind(R.id.textVoiceSize)
    TextView textVoiceSize;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    SRTask mData;

    SRTask.Finish mFinish;

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
        mFinish = mData.finish.get(0);
        textScore.setText(mFinish.score + "");
        textComment.setText(mFinish.comment);
        textTime.setText("完成时间 " + ZYDateUtils.getTimeString(Long.parseLong(mData.create_time) * 1000, ZYDateUtils.MMDDHHMM12));

        if (!TextUtils.isEmpty(mFinish.comment_audio)) {
            layoutVoice.setVisibility(View.VISIBLE);
            textVoiceSize.setText(ZYUtils.getShowHourMinuteSecond((int) mFinish.getAudioTime()));
        }
    }

    @OnClick({R.id.layoutVoice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutVoice:
                if (ZYAudioPlayManager.getInstance().isStartPlay()) {
                    ZYAudioPlayManager.getInstance().startOrPuase();
                } else {
                    ZYAudioPlayManager.getInstance().play(mFinish.comment_audio);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(ZYAudionPlayEvent playEvent) {
        if (playEvent != null) {
            if (mFinish.comment_audio.equals(playEvent.url)) {
                if (playEvent.state == ZYAudioPlayManager.STATE_ERROR ||
                        playEvent.state == ZYAudioPlayManager.STATE_PAUSED ||
                        playEvent.state == ZYAudioPlayManager.STATE_COMPLETED ||
                        playEvent.state == ZYAudioPlayManager.STATE_STOP) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZYAudioPlayManager.getInstance().stop();
    }
}
