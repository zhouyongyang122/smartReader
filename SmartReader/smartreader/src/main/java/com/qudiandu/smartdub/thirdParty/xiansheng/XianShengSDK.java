package com.qudiandu.smartdub.thirdParty.xiansheng;

import android.app.Activity;

import com.constraint.CoreProvideTypeEnum;
import com.qudiandu.smartdub.SRApplication;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.utils.ZYLog;
import com.xs.SingEngine;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/6/29$ 上午11:47$
 */
public class XianShengSDK {

    final String TAG = "XianShengSDK";

    static XianShengSDK instance;

    private XianShengSDK.MarkListener listener;

    private long maxTime;

    private long currentTime;

    private Timer timer;

    SingEngine mSingEngine;

    String saveWavPath;

    private XianShengSDK() {

    }

    public static XianShengSDK getInstance() {
        if (instance == null) {
            instance = new XianShengSDK();
        }
        return instance;
    }

    public void init(final Activity context) {

        try {
            mSingEngine = SingEngine.newInstance(context.getApplicationContext());
            mSingEngine.setListener(new SingEngine.ResultListener() {
                @Override
                public void onBegin() {
                    startTimer();
                }

                @Override
                public void onResult(final JSONObject jsonObject) {

                    ZYLog.e(TAG, "onResult :" + jsonObject.toString());

                    SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                if (jsonObject != null) {
                                    XSBean bean = XSBean.createXSBean(String.valueOf(jsonObject));
                                    if (bean == null || bean.result == null) {
                                        listener.xfMarkError("打分失败,请重新尝试!");
                                    } else {
                                        listener.xfMarkEnd(bean, String.valueOf(jsonObject));
                                    }
                                } else {
                                    listener.xfMarkError("打分失败,请重新尝试!");
                                }
                            }
                        }
                    });
                }

                @Override
                public void onEnd(int i, String s) {
                    final String error_msg = s;
                    ZYLog.e(TAG, "onEnd :" + i + ":" + error_msg);
                    if (i != 0) {
                        SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.xfMarkError(error_msg);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onUpdateVolume(int i) {

                }

                @Override
                public void onFrontVadTimeOut() {
                    final String error_msg = "评分失败，录音超时!";

                    ZYLog.e(TAG, "onFrontVadTimeOut :" + error_msg);

                    SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.xfMarkError(error_msg);
                            }
                        }
                    });
                }

                @Override
                public void onBackVadTimeOut() {
                    final String error_msg = "评分失败，录音超时!";

                    ZYLog.e(TAG, "onBackVadTimeOut :" + error_msg);

                    SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.xfMarkError(error_msg);
                            }
                        }
                    });
                }

                @Override
                public void onRecordingBuffer(byte[] bytes, int i) {

                }

                @Override
                public void onRecordStop() {
                    ZYLog.e(TAG, "onRecordStop");
                }

                @Override
                public void onRecordLengthOut() {
                    final String error_msg = "评分失败，录音超长!";

                    ZYLog.e(TAG, "onRecordLengthOut :" + error_msg);

                    SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.xfMarkError(error_msg);
                            }
                        }
                    });
                }

                @Override
                public void onReady() {
                    ZYLog.e(TAG, "语音评测初使化成功!");
                }

                @Override
                public void onPlayCompeleted() {

                }
            });
            mSingEngine.setServerType(CoreProvideTypeEnum.CLOUD);
            mSingEngine.setLogEnable(1);
            //  设置引擎类型
//            mSingEngine.setServerType("cloud");
            //  设置是否开启VAD功能
            //engine.setOpenVad(true, null);
//            mSingEngine.setOpenVad(true, "vad.0.1.bin");
//            mSingEngine.setFrontVadTime(10000);
//            mSingEngine.setBackVadTime(10000);
            mSingEngine.setWavPath(SRApplication.TRACT_AUDIO_ROOT_DIR);
            mSingEngine.setLogLevel(4);

            JSONObject cfg_init;
            cfg_init = mSingEngine.buildInitJson("a133", "3c6cb028f3e6477ab74acbafbfa7cac2");
            mSingEngine.setNewCfg(cfg_init);
            mSingEngine.newEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancle() {
        try {
            listener = null;
            cancleTimer();
            if (mSingEngine != null) {
                mSingEngine.cancel();
            }
        } catch (Exception e) {

        }
    }

    public void stop() {
        try {
            ZYLog.e(TAG, "stop:录音已停...: " + mSingEngine.getWavPath());

            mSingEngine.stop();

            if (listener != null) {
                listener.xfMarkStart();
            }
            if (listener != null) {
//                File file = new File(mIse.getWavPath());
//                File[] files = file.listFiles();
//                if (files.length > 0) {
//                    File pcmFile = files[0];
//                    File wavFile = new File(saveWavPath);
//                    wavFile.delete();
//                    wavFile.createNewFile();
//                    new PcmToWavUtil().pcmToWav(pcmFile.getAbsolutePath(), wavFile.getAbsolutePath());
//
//                    ZYLog.e(TAG, "audio-wav-path: " + saveWavPath);
//                }

//                ZYFileUtils.copy(saveWavPath, mIse.getWavPath());

//                ZYLog.e(TAG, "audio-wav-path: " + saveWavPath);
//                FileUtils.copyFile(mIse.getWavPath(), saveWavPath);
                listener.xfRecordEnd(mSingEngine.getWavPath());
            }
        } catch (Exception e) {
            ZYLog.e(TAG, "stop-error: " + e.getMessage());
        }
    }

    public void start(XianShengSDK.MarkListener listener, String saveWavPath) {
        try {
            cancle();
            this.listener = listener;
            this.saveWavPath = saveWavPath;
            currentTime = 0;
            maxTime = listener.xfRecordTime();

            JSONObject request = new JSONObject();

            request.put("coreType", "en.sent.score");
            request.put("refText", listener.xfMarkString());
            request.put("rank", 100);
            request.put("outputPhones", 1);
            JSONObject startCfg = mSingEngine.buildStartJson(SRUserManager.getInstance().getUser().uid, request);
            mSingEngine.setStartCfg(startCfg);
            mSingEngine.start();
            ZYLog.e(TAG, "start...: " + request.toString());
            if (listener != null) {
                listener.xfRecordStart();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.xfMarkError(e.getMessage());
            }
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
                    SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.xfRecordProgress(currentTime, maxTime);
                            }
                        }
                    });
                    if (currentTime >= maxTime) {
                        SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stop();
                            }
                        });
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

    public void onDestroy() {
        try {
            listener = null;
            cancleTimer();
            cancle();
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
