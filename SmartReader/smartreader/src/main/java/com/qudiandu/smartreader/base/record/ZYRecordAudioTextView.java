package com.qudiandu.smartreader.base.record;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.player.ZYAudioPlayManager;
import com.qudiandu.smartreader.utils.ZYFileUtils;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import java.io.File;

/**
 * Created by ZY on 17/9/5.
 */

public class ZYRecordAudioTextView extends TextView implements ZYRecordAudioListener {
    protected static final String TAG = "ZYRecordAudioTextView";
    private Dialog mRecordDialog;
    private ImageView mImgRecordStatus;
    private TextView mTextRecordStatus;
    private String mRecordPath;
    private boolean isRecordInStarting = false;
    private boolean isRecordInStoping = false;
    private boolean mPreValid = false;
    private boolean mCurValid = false;
    boolean isInit = false;//三种构造器可能相互调用，重复初始化。。。
    private AsyncTask mStartRecordTask;
    private RecordAudioViewListener mListener;

    public ZYRecordAudioTextView(Context context) {
        super(context);
        init();
    }

    public ZYRecordAudioTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZYRecordAudioTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        if (isInit) {
            return;
        }
        isInit = true;

        if (mRecordDialog == null) {
            mRecordDialog = new Dialog(getContext(), R.style.SRDialogStyle);
            mRecordDialog.setCanceledOnTouchOutside(false);
            mRecordDialog.setContentView(createContentView());
            WindowManager.LayoutParams params = mRecordDialog.getWindow().getAttributes();
            int px = ZYScreenUtils.dp2px(getContext(), 145);
            params.width = px;
            params.height = px;
            mRecordDialog.getWindow().setAttributes(params);
        }

        setText(R.string.text_press_to_speak);
    }

    View createContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.zy_dailog_audio_record, null);
        mImgRecordStatus = (ImageView) view.findViewById(R.id.iv_record_status);
        mTextRecordStatus = (TextView) view.findViewById(R.id.tv_record_status);
        return view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleRecordBtnDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleRecordBtnMove(event);
                break;
            case MotionEvent.ACTION_UP:
                handleRecordBtnUp(event, false);
                break;
            case MotionEvent.ACTION_CANCEL:
                handleRecordBtnUp(event, true);
                break;
        }
        return true;
    }

    private void handleRecordBtnDown(MotionEvent event) {
        ZYAudioPlayManager.getInstance().stop();
        showValidView();
        if (mRecordDialog != null) {
            mRecordDialog.show();
        }
        if (!isRecordInStarting) {
            startRecord();
        }
    }

    private void handleRecordBtnMove(MotionEvent event) {
        if (mCurValid != mPreValid) {
            return;
        }
        mCurValid = isMoveValid(event);
        if (isChanageed()) {
            if (mCurValid) {
                showValidView();
            } else {
                showInvalidView();
            }
            mPreValid = mCurValid;
        }
    }

    private void handleRecordBtnUp(MotionEvent event, boolean cancel) {
        if (mStartRecordTask != null) {
            mStartRecordTask.cancel(true);
            isRecordInStarting = false;
        }
        if (isRecordInStarting) {
            // 显示时间太短
            showRecordShortView();
            // 异步线程停止录音
            stopRecord(false);
        } else {
            boolean isSave = false;
            if (isMoveValid(event) && !cancel) {
                // 保存录音文件并发送
                isSave = true;
            } else {
                // 删除录音文件
                isSave = false;
            }
            if (!isRecordInStoping) {
                stopRecord(isSave);
            }
        }
        mCurValid = false;
    }

    //显示正在录音
    private void showValidView() {
        mImgRecordStatus.setImageLevel(2);
        mTextRecordStatus.setText(R.string.text_recording_now);
        setText(R.string.text_recording_now2);
        mCurValid = true;
        mPreValid = mCurValid;
    }

    //显示松手取消发送
    private void showInvalidView() {
        mImgRecordStatus.setImageLevel(9);
        mTextRecordStatus.setText(R.string.text_recording_cancel);
        setText(R.string.text_press_to_speak);
    }

    //显示录制时间太短，发送失败
    private void showRecordShortView() {
        mImgRecordStatus.setImageLevel(1);
        mTextRecordStatus.setText(R.string.text_speak_time_short);
        setText(R.string.text_press_to_speak);
    }

    //显示抬起手指视图，PS：还不知道显示什么鬼..
    private void showRecordUpView() {
        setText(R.string.text_press_to_speak);
        if (mRecordDialog != null) {
            mRecordDialog.dismiss();
        }
    }

    @Override
    public void onRecordAudioTimeChanged(int time) {

    }

    @Override
    public void onAudioAmplitudeChanged(int db) {
        if (mCurValid) {
            mImgRecordStatus.setImageLevel(db + 1);
        }
    }

    private void startRecord() {
        try {
            mStartRecordTask.cancel(true);
        } catch (Exception e) {

        }
        mStartRecordTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isRecordInStarting = true;
            }

            @Override
            protected String doInBackground(Void... params) {
                mRecordPath = SRApplication.TASK_AUDIO_ROOT_DIR + System.currentTimeMillis() + ".aac";
                ZYLog.d(TAG, "startRecord filePath:" + mRecordPath);
                if (mRecordPath == null || mRecordPath.isEmpty()) {
                    ZYLog.d(TAG, "startRecord mRecordPath == null");
                    return "录音文件创建失败,请查看SDcard空间是否足够!";
                }
                ZYRecordAudioManager.getInstance().setRecordAudioListener(ZYRecordAudioTextView.this);
                //开始采集声音
                boolean ret = ZYRecordAudioManager.getInstance().startRecord(mRecordPath);
                if (!ret) {
                    ZYLog.d(TAG, "startRecord startRecordAudioFile fail");
                    return "录音系统出错,请重新尝试";
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                ZYLog.d(TAG, "startRecord onPostExecute result " + result);
                super.onPostExecute(result);
                if (result == null) {
                    if (mListener != null) {
                        mListener.onAudioRecordStart();
                    }
                } else {
                    if (mListener != null) {
                        mListener.onAudioRecordError(result);
                    }
                }
                isRecordInStarting = false;
            }

            @Override
            protected void onCancelled() {
                isRecordInStarting = false;
                super.onCancelled();
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void stopRecord(final boolean isSave) {

        new AsyncTask<Void, Void, Boolean>() {

            protected void onPreExecute() {
                isRecordInStoping = true;// 设置正在停止录音
                if (ZYRecordAudioManager.getInstance().getRecordLen() < 1) {
                    showRecordShortView();
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                ZYRecordAudioManager.getInstance().stopRecord();
                return true;
            }

            protected void onPostExecute(Boolean result) {
                showRecordUpView();
                //  1.2 录音长度小于1秒
                if (ZYRecordAudioManager.getInstance().getRecordLen() < 1 || !isSave) {
                    ZYLog.d(TAG, "stopRecord 录音成功时,录音时间小于1秒......");
                    ZYFileUtils.delete(mRecordPath);
                } else {
                    File file = new File(mRecordPath);
                    if (!file.exists() || file.length() < 1) {
                        ZYFileUtils.delete(mRecordPath);
                        if (mListener != null) {
                            mListener.onAudioRecordError("录音文件损坏,请重新尝试!");
                        }
                    } else if (mListener != null) {//成功录制，发送
                        mListener.onAudioRecordComplete(mRecordPath, ZYRecordAudioManager.getInstance().getRecordLen());
                    }
                }
                isRecordInStoping = false;
                if (mListener != null) {
                    mListener.onAudioRecordStop();
                }
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isMoveValid(MotionEvent event) {
        if (event == null) {
            return false;
        }
        if (event.getX() > 0 && event.getY() > 0) {
            return true;
        }
        return false;
    }

    private boolean isChanageed() {

        if (mCurValid != mPreValid) {
            return true;
        }

        return false;
    }

    public void onDestroy() {
        ZYRecordAudioManager.getInstance().setRecordAudioListener(null);
    }

    public void setListener(RecordAudioViewListener listener) {
        mListener = listener;
    }

    public interface RecordAudioViewListener {

        void onAudioRecordStart();

        void onAudioRecordStop();

        void onAudioRecordError(String msg);

        void onAudioRecordComplete(String filePath, int durationSe);
    }
}
