package com.qudiandu.smartdub.ui.main.model;

import android.os.Handler;
import android.os.Message;

import com.qudiandu.smartdub.SRApplication;
import com.qudiandu.smartdub.base.player.ZYAudioPlayer;
import com.qudiandu.smartdub.base.player.ZYIPlayer;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.utils.ZYLog;

import java.util.ArrayList;

/**
 * Created by ZY on 17/3/30.
 */

public class SRPlayManager implements ZYIPlayer.PlayerCallBack {

    private static SRPlayManager instance;

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

    private PagePlayListener pagePlayListener;

    private boolean needRepeats = true;

    private Runnable endRunnable = new Runnable() {
        @Override
        public void run() {
            pauseAudio();
            if (pagePlayListener != null) {
                pagePlayListener.onTractPlayComplete(null);
            }
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

    private SRPlayManager() {
        audioPlayer = new ZYAudioPlayer(SRApplication.getInstance(), ZYAudioPlayer.class.getSimpleName());
        audioPlayer.setPlayerCallBack(this);
    }

    public static SRPlayManager getInstance() {
        if (instance == null) {
            instance = new SRPlayManager();
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
        handler.postDelayed(endRunnable, end - start + 50);
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

    public void startAudio(String audioPath, int seekTo) {
        if (audioPath == null) {
            return;
        }
        ZYLog.e(getClass().getSimpleName(), "startAudio: " + audioPath);
        lastAudioPath = audioPath;
        isPause = false;
        audioPlayer.open(audioPath, seekTo);
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

    public void startRepeats(ArrayList<SRTract> tracts, boolean needRepeats) {
        this.needRepeats = needRepeats;
        startRepeats(tracts);
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
                if (!needRepeats) {
                    if (pagePlayListener != null) {
                        pagePlayListener.onTractPlayComplete(repeatTracts.get(repeatTracts.size() - 1));
                    }
                    stopRepeats();
                    return;
                }
            }
            if (repeatTracts.size() <= 0) {
                return;
            }
            SRTract tract = repeatTracts.get(currentRepeatPosition);

            if (pagePlayListener != null) {
                pagePlayListener.onRepeatsTractPlay(tract);
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
            handler.postDelayed(repeatRunnable, end - start + 50);
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
                stopAudio();
                break;
        }
        return true;
    }

    public void setPagePlayListener(PagePlayListener pagePlayListener) {
        this.pagePlayListener = pagePlayListener;
    }

    public interface PagePlayListener {
        void onRepeatsTractPlay(SRTract tract);

        void onTractPlayComplete(SRTract tract);
    }
}
