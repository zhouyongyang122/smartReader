package com.qudiandu.smartdub.ui.main.model;

import android.content.res.AssetFileDescriptor;
import android.os.Handler;
import android.os.Message;

import com.qudiandu.smartdub.SRApplication;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.utils.ZYLog;

import java.util.ArrayList;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.qudiandu.smartdub.SRApplication.BOOK_ROOT_DIR;

public class SRIJKPlayManager {

    public static final String DEF_BOOK_MP3_PATH = BOOK_ROOT_DIR + "-1/mp3";

    private static SRIJKPlayManager instance;

    private IjkMediaPlayer audioPlayer;

    private String lastAudioPath;

    private ArrayList<SRTract> repeatTracts = new ArrayList<SRTract>();

    private int currentRepeatPosition = 0;

    private float mSpeed = 1.0f;

    private int mSeek = -1;

    private SRTract curPlayTrack;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private CollationIJKPlayerListener pagePlayListener;

    private boolean needRepeats = true;

    private Runnable endRunnable = new Runnable() {
        @Override
        public void run() {
            pauseAudio();
            if (pagePlayListener != null) {
                pagePlayListener.onTractPlayComplete(curPlayTrack);
            }
        }
    };

    private boolean isPause;

    private Runnable repeatRunnable = new Runnable() {
        @Override
        public void run() {

            if (pagePlayListener != null) {
                pagePlayListener.onTractRepeatsPlay(curPlayTrack);
            }

            currentRepeatPosition++;
            if (!needRepeats && currentRepeatPosition >= repeatTracts.size()) {
                stopRepeats();
                return;
            }
            if (repeatTracts.size() > 0 && !isPause) {
                starRepeatAudios();
            }
        }
    };

    private SRIJKPlayManager() {
        IjkMediaPlayer.loadLibrariesOnce(null);
    }

    public static SRIJKPlayManager getInstance() {
        if (instance == null) {
            instance = new SRIJKPlayManager();
        }
        return instance;
    }

    void initPlayer() {
        stopAudio();
        audioPlayer = new IjkMediaPlayer();
        audioPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                if (pagePlayListener != null) {
                    pagePlayListener.onTractPlayError(curPlayTrack);
                }
                initPlayer();
                setEndRunnable(repeatTracts.size() > 0 ? repeatRunnable : endRunnable, curPlayTrack.getAudioStart(), curPlayTrack.getAudioEnd());
                return true;
            }
        });
        audioPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                if (mSeek > 0) {
                    audioPlayer.seekTo(mSeek);
                } else {
                    audioPlayer.start();
                    setEndRunnable(repeatTracts.size() > 0 ? repeatRunnable : endRunnable, curPlayTrack.getAudioStart(), curPlayTrack.getAudioEnd());
                }
            }
        });

        audioPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                audioPlayer.start();
                setEndRunnable(repeatTracts.size() > 0 ? repeatRunnable : endRunnable, curPlayTrack.getAudioStart(), curPlayTrack.getAudioEnd());
            }
        });
        audioPlayer.setSpeed(mSpeed);
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
        stopAudio();
    }

    public void startAudio(SRTract trackBean) {
        if (trackBean != null) {
            repeatTracts.clear();
            curPlayTrack = trackBean;
            startAudio(curPlayTrack.getMp3Path(), curPlayTrack.getAudioStart(), curPlayTrack.getAudioEnd(), endRunnable);
        }
    }

    void startAudio(String audioPath, int start, int end, Runnable runnable) {
        try {
            if (audioPath == null) {
                return;
            }
            isPause = false;
            ZYLog.e(getClass().getSimpleName(), "startAudio: " + audioPath + "  ;  " + start + ":" + end);
            if (audioPath.equals(lastAudioPath)) {
                audioPlayer.pause();
                audioPlayer.seekTo(start);
            } else {
                initPlayer();
                mSeek = start;
                lastAudioPath = audioPath;

                audioPlayer.setDataSource(audioPath);
                audioPlayer.prepareAsync();
            }
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "startAudio: " + e.getMessage());
        }
    }

    void setEndRunnable(Runnable runnable, int start, int end) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, endTime((float) (end - start)));
    }

    int endTime(float endTime) {
        return (int) (endTime / mSpeed);
    }

    public void startRepeats(ArrayList<SRTract> tracts, int position, boolean needRepeats) {
        if (tracts == null || tracts.size() <= 0) {
            return;
        }
        this.needRepeats = needRepeats;
        stopAudio();
        clearRepeatTracts();
        currentRepeatPosition = position;
        repeatTracts.addAll(tracts);
        starRepeatAudios();
    }

    public void continueStartRepeats() {
        starRepeatAudios();
    }

    public void stopRepeats() {
        clearRepeatTracts();
        currentRepeatPosition = 0;
        this.needRepeats = true;
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
            curPlayTrack = repeatTracts.get(currentRepeatPosition);

            if (pagePlayListener != null) {
                pagePlayListener.onTractRepeatsPlay(curPlayTrack);
            }
            String audioPath = curPlayTrack.getMp3Path();
            int start = curPlayTrack.getAudioStart();
            int end = curPlayTrack.getAudioEnd();
            startAudio(audioPath, start, end, repeatRunnable);
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
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.pause();
        }
    }

    public void stopAudio() {
        mSeek = -1;
        handler.removeCallbacks(endRunnable);
        handler.removeCallbacks(repeatRunnable);
        if (audioPlayer != null) {
            if (audioPlayer.isPlaying()) {
                audioPlayer.stop();
            }
            audioPlayer.release();
        }
        lastAudioPath = null;
    }

    public void setPagePlayListener(CollationIJKPlayerListener pagePlayListener) {
        this.pagePlayListener = pagePlayListener;
    }

    public interface CollationIJKPlayerListener {

        void onTractRepeatsPlay(SRTract tract);

        void onTractPlayComplete(SRTract tract);

        void onTractPlayError(SRTract tract);
    }
}
