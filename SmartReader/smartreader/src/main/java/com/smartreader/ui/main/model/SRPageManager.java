package com.smartreader.ui.main.model;

import android.os.Handler;
import android.os.Message;

import com.smartreader.SRApplication;
import com.smartreader.base.player.ZYAudioPlayer;
import com.smartreader.base.player.ZYIPlayer;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.utils.ZYLog;

import java.util.ArrayList;

/**
 * Created by ZY on 17/3/30.
 */

public class SRPageManager implements ZYIPlayer.PlayerCallBack {

    private static SRPageManager instance;

    private ZYAudioPlayer audioPlayer;

    private String lastAudioPath;

    private ArrayList<SRTract> repeatTracts = new ArrayList<SRTract>();

    private int currentRepeatPosition = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private RepeatsPlayListener repeatsPlayListener;

    private Runnable endRunnable = new Runnable() {
        @Override
        public void run() {
            pauseAudio();
        }
    };

    private boolean isPause;

    private Runnable repeatRunnable = new Runnable() {
        @Override
        public void run() {
            currentRepeatPosition++;
            if (repeatTracts.size() > 0 && !isPause) {
                starRepeatAudios();
            }
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
        isPause = false;
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
        isPause = false;
        audioPlayer.open(audioPath);
    }

    public void startRepeats(ArrayList<SRTract> tracts) {
        if (tracts == null || tracts.size() <= 0) {
            return;
        }
        stopAudio();
        clearRepeatTracts();
        currentRepeatPosition = 0;
        repeatTracts.addAll(tracts);
        starRepeatAudios();
    }

    public void continueStartRepeats() {
        starRepeatAudios();
    }

    public void stopRepeats() {
        clearRepeatTracts();
        stopAudio();
    }

    private void starRepeatAudios() {
        try {
            if (currentRepeatPosition >= repeatTracts.size()) {
                currentRepeatPosition = 0;
            }
            if (repeatTracts.size() <= 0) {
                return;
            }
            SRTract tract = repeatTracts.get(currentRepeatPosition);

            if (repeatsPlayListener != null) {
                repeatsPlayListener.onRepeatsTractPlay(tract);
            }

            String audioPath = tract.getMp3Path();
            int start = tract.getAudioStart();
            int end = tract.getAudioEnd();
            isPause = false;
            if (lastAudioPath != null && lastAudioPath.equals(audioPath)) {
                audioPlayer.seekTo(start);
            } else {
                lastAudioPath = audioPath;
                audioPlayer.open(audioPath, start);
            }
            handler.removeCallbacks(repeatRunnable);
            handler.postDelayed(repeatRunnable, end - start);
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "starRepeatAudios:error " + e.getMessage());
        }
    }

    public void clearRepeatTracts() {
        repeatTracts.clear();
    }

    public void pauseAudio() {
        isPause = true;
        handler.removeCallbacks(endRunnable);
        handler.removeCallbacks(repeatRunnable);
        audioPlayer.pause();
    }

    public void stopAudio() {
        clearRepeatTracts();
        handler.removeCallbacks(endRunnable);
        handler.removeCallbacks(repeatRunnable);
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

    public void setRepeatsPlayListener(RepeatsPlayListener repeatsPlayListener) {
        this.repeatsPlayListener = repeatsPlayListener;
    }

    public interface RepeatsPlayListener {
        void onRepeatsTractPlay(SRTract tract);
    }
}
