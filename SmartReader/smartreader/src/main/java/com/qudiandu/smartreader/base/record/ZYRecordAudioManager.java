package com.qudiandu.smartreader.base.record;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;

import com.qudiandu.smartreader.utils.ZYLog;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ZYRecordAudioManager {
    private static final String TAG = "ZYRecordAudioManager";

    private static final float minAngle = (float) Math.PI / 8;

    private static final float maxAngle = (float) Math.PI * 7 / 8;

    private static final float DROPOFF_STEP = 0.18f;

    private static ZYRecordAudioManager mZYRecordAudioManager = null;

    private static final int SPACE = 200;

    private MediaRecorder mMediaRecorder = null;

    private boolean mRecording = false;

    private ZYRecordAudioListener mRecordAudioListener;

    private Timer mTimer;

    private TimerTask mTimerTask;

    private int mRecordLen = 0;

    private ZYRecordAudioManager() {
    }

    public synchronized static ZYRecordAudioManager getInstance() {
        if (mZYRecordAudioManager == null) {
            mZYRecordAudioManager = new ZYRecordAudioManager();
        }
        return mZYRecordAudioManager;
    }

    public int getRecordLen() {
        return getSecFromMillSec(mRecordLen);
    }

    public synchronized boolean startRecord(String audioPath) {
        if (audioPath == null || audioPath.isEmpty()) {
            ZYLog.e(TAG, "startRecordAudioFile audioPath == null");
            return false;
        }

        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        } else {
            mMediaRecorder.reset();
        }

        ZYLog.e(TAG, "record audioPath:" + audioPath);
        try {
//                //如果是即时通讯使用amr编码
//                mMediaRecorder.setAudioSamplingRate(8000);
//                mMediaRecorder.setAudioEncodingBitRate(7950);
//                mMediaRecorder.setAudioChannels(1);
//                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //如果是其他用aac编码
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setOutputFile(audioPath);
            mMediaRecorder.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    ZYLog.e(TAG, "record error:" + what + ":" + extra);
                }
            });
            mMediaRecorder.prepare();
            // 开始录音
            mMediaRecorder.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        mRecordLen = 0;
        startTimer();
        mRecording = true;
        ZYLog.e(TAG, "startRecord success");
        return true;
    }

    public synchronized void stopRecord() {
        stopTimer();
        if (mRecording) {
            if (mMediaRecorder != null) {
                try {
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    mMediaRecorder.release();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                mMediaRecorder = null;
            }
        }
        mRecording = false;
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                int level = 0;
                float currentAngle = 0;
                float angle = minAngle;
                int maxLevel = 7;
                if (mMediaRecorder != null) {
                    try {
                        angle += (float) (maxAngle - minAngle) * mMediaRecorder.getMaxAmplitude() / 32768;
                    } catch (IllegalStateException e) {
                        // TODO: handle exception
                        angle = minAngle;
                    }
                }
                if (angle > currentAngle) {
                    currentAngle = angle;
                } else {
                    currentAngle = Math.max(angle, currentAngle - DROPOFF_STEP);
                }
                currentAngle = Math.min(maxAngle, currentAngle);
                if (currentAngle <= 0) {
                    level = 0;
                } else {
                    level = (int) Math.ceil(maxLevel * currentAngle / 3.0f);
                }
                mRecordLen += SPACE;

                if (mRecording) {
                    mRecordAudioListener.onAudioAmplitudeChanged(level);
                    mRecordAudioListener.onRecordAudioTimeChanged(mRecordLen);
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, SPACE);
    }

    /**
     * 停止定时器，释放资源
     *
     * @since V1.0
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private int getSecFromMillSec(int recordLen) {
        int len = recordLen / 1000;
        int len2 = recordLen % 1000;
        if (len2 > 500) {
            len = len + 1;
        }
        return len;
    }

    public void setRecordAudioListener(ZYRecordAudioListener recordAudioListener) {
        mRecordAudioListener = recordAudioListener;
    }
}
