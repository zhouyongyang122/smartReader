package com.qudiandu.smartreader.ui.task.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.event.ZYAudionPlayEvent;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.player.ZYAudioPlayManager;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.task.activity.SRTaskListenActivity;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskListenHistory;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Formatter;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/9/20.
 */

public class SRTaskListenFragment extends ZYBaseFragment implements Runnable {

    SRTaskProblem mTaskProblem;

    SRTaskProblem.Problem mProblem;

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

    @Bind(R.id.layoutAction)
    RelativeLayout layoutAction;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textPre)
    TextView textPre;

    @Bind(R.id.textNext)
    TextView textNext;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textGrade)
    TextView textGrade;

    @Bind(R.id.textTime)
    TextView textTime;

    boolean mIsStartPlay;

    // 时间格式器 用来格式化视频播放的时间
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    boolean mHasPre;

    boolean mHasNext;

    Animation mAnima;

    SRTaskFinish mTaskFinish;

    SRTaskListenHistory mHistory;

    final int TIME = 500;

    ListenTimeRunnable mRunnable;

    public void init(SRTaskProblem taskProblem, SRTaskProblem.Problem problem, boolean hasPre, boolean hasNext, SRTaskFinish taskFinish) {
        mTaskProblem = taskProblem;
        mProblem = problem;
        mHasPre = hasPre;
        mHasNext = hasNext;
        mTaskFinish = taskFinish;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_task_listen, container, false);
        ButterKnife.bind(this, view);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mAnima = AnimationUtils.loadAnimation(mActivity, R.anim.play_rotate);

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

        mHistory = SRTaskListenHistory.queryById(mProblem.problem_id + "");

        mRunnable = new ListenTimeRunnable();

        showView();

        return view;
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

        if (!mHasNext && !mHasPre) {
            layoutAction.setVisibility(View.INVISIBLE);
        } else {
            if (!mHasNext) {
                textNext.setVisibility(View.GONE);
            }

            if (!mHasPre) {
                textPre.setVisibility(View.GONE);
            }
        }

        if (mTaskFinish == null) {
            SRUser user = SRUserManager.getInstance().getUser();
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, user.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(user.nickname);
            textGrade.setText(user.getGrade() + "年级  " + user.age + "岁");
            textTime.setText(stringForTime(mHistory.listenTime / 1000));
        } else {
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, mTaskFinish.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(mTaskFinish.nickname);
            textGrade.setText("1年级  " + "10岁");
            textTime.setText(stringForTime(mProblem.getTimeAnswer()));
        }

        textTitle.setText(mProblem.title);
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
            }, 1000);
            imgPlay.postDelayed(this, 3000);

            if (!SRUserManager.getInstance().getUser().isTeacher()) {
                seekBar.postDelayed(mRunnable, TIME);
            }
        } else {
            imgPlay.removeCallbacks(this);
            imgPlay.setVisibility(View.VISIBLE);
            imgPlay.setImageResource(R.drawable.icon_play_big);
            imgPlaySmall.setImageResource(R.drawable.icon_play);
            imgBg.clearAnimation();
            seekBar.removeCallbacks(mRunnable);
        }
    }

    @OnClick({R.id.imgPlay, R.id.imgPlaySmall, R.id.layoutRoot, R.id.textNext, R.id.textPre})
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
            case R.id.textNext:
                ((SRTaskListenActivity) mActivity).next();
                break;
            case R.id.textPre:
                ((SRTaskListenActivity) mActivity).pre();
                break;
        }
    }

    public void play() {
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
        if (playEvent.url.equals(mProblem.audio)) {
            if (playEvent.state == ZYAudioPlayManager.STATE_STOP) {
                mIsStartPlay = false;
                refreshPlayState(false);
            }
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!SRUserManager.getInstance().getUser().isTeacher()) {
            if (mHistory != null) {
                mHistory.update();
            }
        }
    }

    @Override
    public void run() {
        try {
            imgPlay.setVisibility(View.GONE);
        } catch (Exception e) {

        }
    }

    public SRTaskProblem.Problem getProblem() {
        return mProblem;
    }

    public int getListenTime() {
        return mHistory.listenTime;
    }

    public class ListenTimeRunnable implements Runnable {
        @Override
        public void run() {
            try {
                mHistory.listenTime += TIME;
                seekBar.postDelayed(mRunnable, TIME);
                textTime.setText(stringForTime(mHistory.listenTime / 1000));
            } catch (Exception e) {

            }
        }
    }
}
