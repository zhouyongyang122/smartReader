package com.qudiandu.smartreader.base.record;

public interface ZYRecordAudioListener {

    /**
     * 录音时间改变
     */
    void onRecordAudioTimeChanged(int time);

    /**
     * 音量大小
     *
     * @param db 分贝值，分为0、1、2、3、4、5,6,7
     * @see [类、类#方法、类#成员]
     */
    void onAudioAmplitudeChanged(int db);

}
