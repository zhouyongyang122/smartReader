package com.smartreader.ui.main.model;

import android.os.Handler;
import android.os.Message;

import com.smartreader.SRApplication;
import com.smartreader.base.player.ZYAudioPlayer;
import com.smartreader.base.player.ZYIPlayer;
import com.smartreader.utils.ZYLog;

/**
 * Created by ZY on 17/3/30.
 */

public class SRPageManager implements ZYIPlayer.PlayerCallBack {

    private static SRPageManager instance;

    private ZYAudioPlayer audioPlayer;

    private String lastAudioPath;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Runnable endRunnable = new Runnable() {
        @Override
        public void run() {
            pauseAudio();
        }
    };

    private SRPageManager() {
        audioPlayer = new ZYAudioPlayer(SRApplication.getInstance(), ZYAudioPlayer.class.getSimpleName());
        audioPlayer.setPlayerCallBack(this);
    }

    public static SRPageManager getInstance() {
        if (instance == null) {
            instance = new SRPageManager();
        }
        return instance;
    }

    public void startAudio(String audioPath, int start, int end) {
        if (audioPath == null) {
            return;
        }
        ZYLog.e(getClass().getSimpleName(), "startAudio: " + audioPath + "  ;  " + start + ":" + end);
        if (lastAudioPath != null && lastAudioPath.equals(audioPath)) {
            audioPlayer.seekTo(start);
        } else {
            lastAudioPath = audioPath;
            audioPlayer.open(audioPath, start);
        }
        handler.removeCallbacks(endRunnable);
        handler.postDelayed(endRunnable, end - start);
    }

    public void startAudio(String audioPath) {
        if (audioPath == null) {
            return;
        }
        ZYLog.e(getClass().getSimpleName(), "startAudio: " + audioPath);
        lastAudioPath = audioPath;
        audioPlayer.open(audioPath);
    }

    public void pauseAudio() {
        audioPlayer.pause();
    }

    public void stopAudio() {
        audioPlayer.stop();
        lastAudioPath = null;
    }

    @Override
    public boolean onCallBack(String tag, int what, String msg, ZYIPlayer player) {
        ZYLog.e(getClass().getSimpleName(), "audio-onCallBack: " + player.getClass().getSimpleName() + ":" + msg);
        switch (what) {
            case ZYIPlayer.PLAYER_PREPARED:
                break;
            case ZYIPlayer.PLAYER_ERROR_SYSTEM:
            case ZYIPlayer.PLAYER_ERROR_UNKNOWN:
            case ZYIPlayer.PLAYER_ERROR_NET:
                lastAudioPath = null;
                break;
        }
        return false;
    }
}
