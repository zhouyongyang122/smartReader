package com.qudiandu.smartreader.thirdParty.xunfei;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.iflytek.cloud.SpeechUtility;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.thirdParty.xiansheng.XSBean;
import com.qudiandu.smartreader.thirdParty.xunfei.xml.XmlResultParser;
import com.qudiandu.smartreader.utils.ZYLog;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ZY on 17/4/3.
 */

public class XunFeiSDK {

    private final String TAG = "XunFeiSDK";

    private static XunFeiSDK instance;

    private SpeechEvaluator mIse;

    private static final String audioType = "wav";//wav pcm

    public static final String XUN_FEI_DIR = SRApplication.APP_ROOT_DIR + File.separator + "xunfei" + File.separator;

    public static final String XUN_FEI_RECORDE_PATH = XUN_FEI_DIR + "audio." + audioType;

    private MarkListener listener;

    private long maxTime;

    private long currentTime;

    private Timer timer;

    private XunFeiSDK() {
        File file = new File(XUN_FEI_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        SpeechUtility.createUtility(SRApplication.getInstance(), "appid=" + "58e1ac82");

        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);
    }

    public static XunFeiSDK getInstance() {
        if (instance == null) {
            instance = new XunFeiSDK();
        }
        return instance;
    }

    public void init(Context context) {
        if (mIse == null) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
//            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            mIse = SpeechEvaluator.createEvaluator(context, new InitListener() {
                @Override
                public void onInit(int i) {
                    ZYLog.e(getClass().getSimpleName(), "onInit: " + i);
                }
            });
            setParams();
        }
    }

    private void setParams() {

        mIse.setParameter(SpeechConstant.LANGUAGE, "en_us");
        mIse.setParameter(SpeechConstant.ISE_CATEGORY, "read_sentence");
        mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mIse.setParameter(SpeechConstant.VAD_BOS, "5000");
        mIse.setParameter(SpeechConstant.VAD_EOS, "1800");
        mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");
        mIse.setParameter(SpeechConstant.RESULT_LEVEL, "plain");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT, audioType);
        mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, XUN_FEI_RECORDE_PATH);
    }

    public void cancle() {
        try {
            listener = null;
            cancleTimer();
            if (mIse != null) {
                mIse.cancel();
            }
        } catch (Exception e) {

        }
    }

    public void stop() {
        try {
            if (mIse.isEvaluating()) {
                ZYLog.e(TAG, "stop:录音已停...: " + Thread.currentThread().getName());

                mIse.stopEvaluating();

                if (listener != null) {
                    listener.xfMarkStart();
                }
                if (listener != null) {
                    listener.xfRecordEnd(XUN_FEI_RECORDE_PATH);
                }
            }
        } catch (Exception e) {
            ZYLog.e(TAG, "stop-error: " + e.getMessage());
        }
    }

    public void start(MarkListener listener) {
        cancle();
        this.listener = listener;
        currentTime = 0;
        maxTime = listener.xfRecordTime();

        mIse.startEvaluating(listener.xfMarkString(), null, mEvaluatorListener);

        if (listener != null) {
            listener.xfRecordStart();
        }
    }

    private void startTimer() {
        cancleTimer();
        try {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    currentTime += 100;
                    if (listener != null) {
                        listener.xfRecordProgress(currentTime, maxTime);
                    }
                    if (currentTime >= maxTime) {
                        stop();
                        cancleTimer();
                    }
                }
            }, 100, 100);
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "record-progress-error: " + e.getMessage());
        }
    }

    private void cancleTimer() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {

        }
    }

    // 评测监听接口
    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            ZYLog.e(TAG, "evaluator result :" + isLast);

            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());
                if (builder != null) {
                    String mLastResult = builder.toString();
                    if (!TextUtils.isEmpty(mLastResult)) {
                        XmlResultParser resultParser = new XmlResultParser();
                        FinalResult finalResult = (FinalResult) resultParser.parse(mLastResult);
                        if (listener != null) {
//                            listener.xfMarkEnd((int) (finalResult.total_score * 100 / 5));
                            return;
                        }
                    }
                }
                if (listener != null) {
                    listener.xfMarkError("评分失败，请重新尝试!");
                }
            }
        }

        @Override
        public void onError(SpeechError error) {
            String error_msg = "评分失败，请重新尝试!";
            if (error != null) {
                error_msg = error.getErrorCode() + "," + error.getErrorDescription();
                ZYLog.e(TAG, "onError :" + error_msg);
            } else {
                ZYLog.e(TAG, "evaluator over");
            }

            if (listener != null) {
                listener.xfMarkError(error_msg);
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            ZYLog.e(TAG, "evaluator begin");
            startTimer();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            ZYLog.e(TAG, "evaluator stoped");
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前音量：" + volume);
            ZYLog.e(TAG, "当前音量是: " + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };

    public void onDestroy() {
        try {
            listener = null;
            cancleTimer();
            cancle();
            if (null != mIse) {
                mIse.destroy();
                mIse = null;
            }
        } catch (Exception e) {

        }
    }

    public interface MarkListener {

        long xfRecordTime();

        String xfMarkString();

        void xfRecordStart();

        void xfRecordProgress(long current, long total);

        void xfRecordEnd(String path);

        void xfMarkStart();

        void xfMarkEnd(XSBean bean, String json);

        void xfMarkError(String error);
    }
}
