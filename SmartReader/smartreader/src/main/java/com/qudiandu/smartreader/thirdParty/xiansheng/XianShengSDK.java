package com.qudiandu.smartreader.thirdParty.xiansheng;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.thirdParty.xunfei.XunFeiSDK;
import com.qudiandu.smartreader.utils.ZYFileUtils;
import com.qudiandu.smartreader.utils.ZYLog;
import com.xs.SingEngine;

import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ZY on 17/6/14.
 */

public class XianShengSDK {

    final String TAG = "XianShengSDK";

    static XianShengSDK instance;

    private XunFeiSDK.MarkListener listener;

    private long maxTime;

    private long currentTime;

    private Timer timer;

    SingEngine mIse;

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
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //  获取引擎实例,设置测评监听对象
                    mIse = SingEngine.newInstance(context);
                    mIse.setListener(new SingEngine.ResultListener() {
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
                                            XSBean bean = new Gson().fromJson(String.valueOf(jsonObject), XSBean.class);
                                            if (bean == null || bean.result == null) {
                                                listener.xfMarkError("打分失败,请重新尝试!");
                                            } else {
                                                listener.xfMarkEnd(bean.result.overall);
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
                    //  设置引擎类型
                    mIse.setServerType("cloud");
                    //  设置是否开启VAD功能
                    //engine.setOpenVad(true, null);
                    mIse.setOpenVad(true, "vad.0.1.bin");
                    mIse.setFrontVadTime(10000);
                    mIse.setBackVadTime(10000);
                    //   构建引擎初始化参数
                    JSONObject cfg_init = mIse.buildInitJson("a133", "3c6cb028f3e6477ab74acbafbfa7cac2");
                    //   设置引擎初始化参数
                    mIse.setNewCfg(cfg_init);
                    //   引擎初始化
                    mIse.newEngine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
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
            ZYLog.e(TAG, "stop:录音已停...: " + mIse.getWavPath());

            mIse.stop();

            if (listener != null) {
                listener.xfMarkStart();
            }
            if (listener != null) {

                File file = new File(mIse.getWavPath());
                File[] files = file.listFiles();
                if (files.length > 0) {
                    File pcmFile = files[0];
                    File wavFile = new File(saveWavPath);
                    wavFile.delete();
                    wavFile.createNewFile();
                    new PcmToWavUtil().pcmToWav(pcmFile.getAbsolutePath(), wavFile.getAbsolutePath());

                    ZYLog.e(TAG, "audio-wav-path: " + saveWavPath);
                }

                listener.xfRecordEnd(saveWavPath);
            }
        } catch (Exception e) {
            ZYLog.e(TAG, "stop-error: " + e.getMessage());
        }
    }

    public void start(XunFeiSDK.MarkListener listener, String saveWavPath) {
        try {
            cancle();
            this.listener = listener;
            this.saveWavPath = saveWavPath;
            currentTime = 0;
            maxTime = listener.xfRecordTime();

            ZYFileUtils.delete(mIse.getWavPath(), false);

            JSONObject request = new JSONObject();
            request.put("coreType", "en.sent.score");
            request.put("refText", listener.xfMarkString());
            request.put("rank", 100);
            //构建评测请求参数
            JSONObject startCfg = mIse.buildStartJson("guest", request);
            //设置评测请求参数
            mIse.setStartCfg(startCfg);
            //开始测评
            mIse.start();


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

}
