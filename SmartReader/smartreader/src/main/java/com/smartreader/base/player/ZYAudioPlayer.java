package com.smartreader.base.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

import com.smartreader.SRApplication;

/**
 * Created by zhouyongyang on 16/11/10.
 */

public class ZYAudioPlayer extends ZYBasePlayer {

    private MediaPlayer mMediaPlayer;

    private boolean canStart = true;

    public ZYAudioPlayer(Context context, String tag) {
        this.mContext = context;
        this.tag = tag;
    }

    @Override
    public void open(String url, int seekTo) {
        if (url == null) {
            return;
        }
        canStart = true;
        currentPosition = seekTo;
        this.url = url;
        release();
        try {
            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnInfoListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            if (url.startsWith("file:///")) {
                AssetFileDescriptor fileDescriptor = SRApplication.getInstance().getAssets().openFd("1/mp3" + url.substring(url.lastIndexOf("/")));
                mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            } else {
                mMediaPlayer.setDataSource(url);
            }
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, ZYIPlayer.PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public void start(boolean needSeek) {
        try {
            if (!canStart) {
                return;
            }
            if (!mMediaPlayer.isPlaying()) {
                if (needSeek && currentPosition > 0) {
                    mMediaPlayer.seekTo(currentPosition);
                }
                mMediaPlayer.start();
                mCurrentState = STATE_PLAYING;
            }
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, ZYIPlayer.PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public void pause() {
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, ZYIPlayer.PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public void seekTo(int seekTo) {
        if (mMediaPlayer != null) {
            currentPosition = seekTo;
            if (mCurrentState == STATE_PLAYING) {
                mMediaPlayer.seekTo(seekTo);
            } else {
                start(true);
            }
        }
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            currentPosition = mMediaPlayer.getCurrentPosition();
        }
        return currentPosition;
    }

    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
    }

    public boolean isCanStart() {
        return canStart;
    }

    @Override
    public void release() {
        cancleTimer();
        if (mMediaPlayer != null) {
            try {
                mCurrentState = STATE_IDLE;
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {

            }
        }

        if (mContext != null) {
            try {
                AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                am.abandonAudioFocus(null);
            } catch (Exception e) {

            }
        }
    }
}
