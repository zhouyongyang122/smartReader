package com.qudiandu.smartreader.base.player;

import android.os.Handler;
import android.text.TextUtils;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.event.ZYAudionPlayEvent;
import com.qudiandu.smartreader.utils.ZYLog;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ZY on 17/9/9.
 */

public class ZYAudioPlayManager implements ZYAudioPlayer.PlayerCallBack {

    //出错状态
    public static int STATE_ERROR = 0;

    //准备播放
    public static int STATE_PREPARING = 1;

    //准备完成,开始播放
    public static int STATE_PREPARED = 2;

    //正在播放
    public static int STATE_PLAYING = 3;

    //暂停中
    public static int STATE_PAUSED = 4;

    //收费视频暂停播放
    public static int STATE_NEED_BUY_PAUSED = 5;

    //缓冲开始
    public static int STATE_BUFFERING_START = 6;

    //缓冲结束
    public static int STATE_BUFFERING_END = 7;

    //准备播放下一音频
    public static int STATE_PREPARING_NEXT = 8;

    //播放完成
    public static int STATE_COMPLETED = 9;

    //停止播放
    public static int STATE_STOP = 10;

    private static ZYAudioPlayManager instance;

    private String mUrl;

    //音频播放器
    ZYAudioPlayer audioPlayer;

    boolean mIsStartPlay;

    Handler handler = new Handler();

    UpdateProgressRunnable updateProgress;


    private ZYAudioPlayManager() {
        updateProgress = new UpdateProgressRunnable();
    }

    public static ZYAudioPlayManager getInstance() {
        if (instance == null) {
            instance = new ZYAudioPlayManager();
        }
        return instance;
    }

    public ZYAudioPlayer getAudioPlayer() {
        if (audioPlayer == null) {
            audioPlayer = new ZYAudioPlayer(SRApplication.getInstance(), "ZYPlayService");
            audioPlayer.setPlayerCallBack(this);
        }
        return audioPlayer;
    }

    public void play(String url) {
        stop();
        mUrl = url;
        stopProgressUpdate();
        sendCallBack(STATE_PREPARING, "播放器初使化中");
        getAudioPlayer().open(url, 0);
        mIsStartPlay = true;
    }

    public void startOrPuase() {
        if (getAudioPlayer().isPlaying()) {
            puase();
        } else {
            start();
        }
    }

    public void start() {
        if (!getAudioPlayer().isPlaying()) {
            stopProgressUpdate();
            startProgressUpdate(0);
            getAudioPlayer().start(true);
            sendCallBack(STATE_PLAYING, "正在播放");
        }
    }

    public void puase() {
        if (getAudioPlayer().isPlaying()) {
            stopProgressUpdate();
            getAudioPlayer().pause();
            sendCallBack(STATE_PAUSED, "暂停播放");
        }
    }

    public void stop() {
        mIsStartPlay = false;
        stopProgressUpdate();
        getAudioPlayer().stop();
        sendCallBack(STATE_STOP, "停止播放");
    }

    public void seekTo(int currentProgress, int totalProgress) {
        float progress = (float) currentProgress / (float) totalProgress;
        getAudioPlayer().seekTo((int) (progress * audioPlayer.getDuration()));
    }

    public boolean isStartPlay() {
        return mIsStartPlay;
    }

    public boolean isSamePlay(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.equals(mUrl);
    }

    /**
     * 是否正在播放中
     *
     * @return
     */
    public boolean isPlaying() {
        if (getAudioPlayer() == null) {
            return false;
        }
        return getAudioPlayer().isPlaying();
    }

    /**
     * 开始进度改变
     *
     * @param delayedTime 启动延迟时间
     */
    private void startProgressUpdate(int delayedTime) {
        if (updateProgress == null) {
            return;
        }

        stopProgressUpdate();
        if (delayedTime > 0) {
            handler.postDelayed(updateProgress, delayedTime);
        } else {
            handler.post(updateProgress);
        }
    }

    /**
     * 停止进度改变
     */
    private void stopProgressUpdate() {
        if (updateProgress != null) {
            handler.removeCallbacks(updateProgress);
        }
    }

    private void sendCallBack(int state, String msg) {
        ZYAudionPlayEvent playEvent = new ZYAudionPlayEvent(state, msg, audioPlayer.getCurrentPosition(), audioPlayer.getDuration());
        playEvent.setUrl(mUrl);
        EventBus.getDefault().post(playEvent);
    }

    @Override
    public boolean onCallBack(String tag, int what, String msg, ZYIPlayer player) {
        ZYLog.e(getClass().getSimpleName(), "ZYAudioPlayManager-onCallBack: " + what + ":" + msg);

        switch (what) {
            case ZYIPlayer.PLAYER_PREPARED:
                startProgressUpdate(0);
                sendCallBack(STATE_PREPARED, "准备播放");
                break;
            case ZYIPlayer.PLAYER_BUFFERING_END:
                startProgressUpdate(0);
                sendCallBack(STATE_BUFFERING_END, "缓冲结束-开始播放");
                break;
            case ZYIPlayer.PLAYER_BUFFERING_START:
                stopProgressUpdate();
                sendCallBack(STATE_BUFFERING_START, "缓冲开始-等待播放");
                break;
            case ZYIPlayer.PLAYER_COMPLETIIONED:
                stopProgressUpdate();
                stop();
                sendCallBack(STATE_COMPLETED, "播放完成");
                break;
            case ZYIPlayer.PLAYER_ERROR_SYSTEM:
            case ZYIPlayer.PLAYER_ERROR_UNKNOWN:
            case ZYIPlayer.PLAYER_ERROR_NET:
                stopProgressUpdate();
                sendCallBack(STATE_ERROR, "播放出错");
                break;
        }
        return true;
    }

    class UpdateProgressRunnable implements Runnable {
        @Override
        public void run() {
            if (audioPlayer != null) {
                sendCallBack(STATE_PLAYING, "正在播放");
                handler.postDelayed(updateProgress, 500);
            }
        }
    }
}
