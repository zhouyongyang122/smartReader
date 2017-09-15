package com.qudiandu.smartreader.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.event.ZYAudionPlayEvent;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.base.player.ZYAudioPlayManager;
import com.qudiandu.smartreader.base.view.ZYLoadingView;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.task.model.SRTaskModel;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Formatter;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/9/9.
 */

public class SRTaskListenActivity extends ZYBaseActivity implements Runnable {

    public static final String TASK_ID = "task_id";

    int mTaskId;

    SRTaskProblem mTaskProblem;

    SRTaskProblem.Problem mProblem;

    ZYLoadingView mLoadingView;

    CompositeSubscription mSubscription;

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.imgPlay)
    ImageView imgPlay;

    @Bind(R.id.textStartTime)
    TextView textStartTime;

    @Bind(R.id.seekBar)
    SeekBar seekBar;

    @Bind(R.id.textEndTime)
    TextView textEndTime;

    @Bind(R.id.imgPlaySmall)
    ImageView imgPlaySmall;

    boolean mIsStartPlay;

    // 时间格式器 用来格式化视频播放的时间
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    Animation mAnima;

    public static Intent createIntent(Context context, int taskId) {
        Intent intent = new Intent(context, SRTaskListenActivity.class);
        intent.putExtra(TASK_ID, taskId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_task_listen);
        initLoadingView();
        mSubscription = new CompositeSubscription();
        mTaskId = getIntent().getIntExtra(TASK_ID, 0);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mLoadingView.showLoading();
        mAnima = AnimationUtils.loadAnimation(this, R.anim.play_rotate);
        loadData();
    }

    private void initLoadingView() {
        mLoadingView = new ZYLoadingView(this);
        mLoadingView.attach(mRootView);
        mLoadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingView.showLoading();
                loadData();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ZYAudioPlayManager.getInstance().seekTo(seekBar.getProgress(), seekBar.getMax());
            }
        });
    }

    private void showView() {
        String pic = null;
        if (TextUtils.isEmpty(mProblem.pic)) {
            pic = mTaskProblem.page_url;
        } else {
            pic = mProblem.pic;
        }
        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgBg, pic);
        refreshProgress(0, (int) (mProblem.getAudioTime() * 1000));
        play();
    }

    void refreshProgress(int currentPosition, int totalPosition) {
        textStartTime.setText(stringForTime(currentPosition / 1000));
        textEndTime.setText(stringForTime(totalPosition / 1000));
        float progress = ((float) currentPosition / (float) totalPosition) * 1000;
        seekBar.setProgress((int) progress);
    }

    void refreshPlayState(boolean isPlaying) {
        if (isPlaying) {
            imgPlay.setImageResource(R.drawable.icon_stop_big);
            imgPlaySmall.setImageResource(R.drawable.icon_stop);
            imgBg.clearAnimation();
            imgBg.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgBg.startAnimation(mAnima);
                }
            },1000);
            imgPlay.postDelayed(this, 3000);
        } else {
            imgPlay.removeCallbacks(this);
            imgPlay.setVisibility(View.VISIBLE);
            imgPlay.setImageResource(R.drawable.icon_play_big);
            imgPlaySmall.setImageResource(R.drawable.icon_play);
            imgBg.clearAnimation();
        }
    }

    private void loadData() {
        mSubscription.add(ZYNetSubscription.subscription(new SRTaskModel().getProblemTaskDetail(mTaskId + ""), new ZYNetSubscriber<ZYResponse<SRTaskProblem>>() {
            @Override
            public void onSuccess(ZYResponse<SRTaskProblem> response) {
                mLoadingView.showNothing();
                mTaskProblem = response.data;
                showTitle(mTaskProblem.title);
                mProblem = mTaskProblem.problems.get(0);
                showView();
            }

            @Override
            public void onFail(String message) {
                mLoadingView.showError();
            }
        }));
    }

    @OnClick({R.id.imgPlay, R.id.imgPlaySmall, R.id.layoutRoot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgPlay:
            case R.id.imgPlaySmall:
                play();
                break;
            case R.id.layoutRoot:
                if (imgPlay.getVisibility() == View.GONE) {
                    imgPlay.removeCallbacks(this);
                    imgPlay.setVisibility(View.VISIBLE);
                    imgPlay.postDelayed(this, 3000);
                }
                break;
        }
    }

    private void play() {
        if (!mIsStartPlay) {
            mIsStartPlay = true;
            ZYAudioPlayManager.getInstance().play(mProblem.audio);
            refreshPlayState(true);
        } else {
            refreshPlayState(!ZYAudioPlayManager.getInstance().isPlaying());
            ZYAudioPlayManager.getInstance().startOrPuase();
        }
    }

    /**
     * 根据秒格式化时间
     *
     * @param timeS
     * @return
     */
    private String stringForTime(int timeS) {
        int seconds = timeS % 60;
        int minutes = (timeS / 60) % 60;
        int hours = timeS / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(ZYAudionPlayEvent playEvent) {
        if (playEvent.state == ZYAudioPlayManager.STATE_ERROR) {
            refreshPlayState(false);
        } else if (playEvent.state == ZYAudioPlayManager.STATE_PAUSED) {
            refreshPlayState(false);
        } else if (playEvent.state == ZYAudioPlayManager.STATE_COMPLETED) {
            refreshPlayState(false);
            mIsStartPlay = false;
        }
        refreshProgress(playEvent.currentDuration, playEvent.totalDuration);
    }

    @Override
    public void run() {
        imgPlay.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        ZYAudioPlayManager.getInstance().stop();
    }
}
