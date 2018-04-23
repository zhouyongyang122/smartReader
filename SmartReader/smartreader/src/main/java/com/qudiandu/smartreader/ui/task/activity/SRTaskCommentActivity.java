package com.qudiandu.smartreader.ui.task.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiniu.rs.CallBack;
import com.qiniu.rs.CallRet;
import com.qiniu.rs.UploadCallRet;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.event.ZYAudionPlayEvent;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.base.player.ZYAudioPlayManager;
import com.qudiandu.smartreader.base.record.ZYRecordAudioTextView;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.mark.view.SRMarkFragment;
import com.qudiandu.smartreader.ui.task.model.SRTaskModel;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;
import com.qudiandu.smartreader.utils.ZYUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskCommentActivity extends ZYBaseActivity {

    static final String FINISH = "finish";

    public static Intent createIntent(Context context, SRTaskFinish taskFinish) {
        Intent intent = new Intent(context, SRTaskCommentActivity.class);
        intent.putExtra(FINISH, taskFinish);
        return intent;
    }

    @Bind(R.id.textMsg)
    EditText textMsg;

    @Bind(R.id.textRecord)
    ZYRecordAudioTextView textRecord;

    @Bind(R.id.layoutVoice)
    RelativeLayout layoutVoice;

    @Bind(R.id.textVoiceSize)
    TextView textVoiceSize;

    @Bind(R.id.layoutVoiceTip)
    LinearLayout layoutVoiceTip;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    SRTaskFinish mFinish;

    String mQiniuKey;

    int mDurationSe;

    String mFilePath;

    CompositeSubscription subscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_task_comment);

        mFinish = (SRTaskFinish) getIntent().getSerializableExtra(FINISH);
        mActionBar.showTitle("评论");
        textMsg.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});

        showActionRightTitle("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comment = textMsg.getText().toString();
                if (TextUtils.isEmpty(mQiniuKey) && TextUtils.isEmpty(comment)) {
                    ZYToast.show(SRTaskCommentActivity.this, "评论内容不能为空");
                    return;
                }
                submitComment(comment, mQiniuKey, mDurationSe + "");
            }
        });

        textRecord.setListener(new ZYRecordAudioTextView.RecordAudioViewListener() {
            @Override
            public void onAudioRecordStart() {

            }

            @Override
            public void onAudioRecordStop() {

            }

            @Override
            public void onAudioRecordError(String msg) {

            }

            @Override
            public void onAudioRecordComplete(final String filePath, final int durationSe) {
                ZYLog.e("SRTaskProblemFragment", "audio-path: " + filePath);
                //先上传音频到七牛
                final File file = new File(filePath);
                if (!file.exists()) {
                    ZYToast.show(SRTaskCommentActivity.this, "音频文件丢失,请重新录音!");
                } else {
                    showProgress();
                    String key = getTime() + File.separator + System.currentTimeMillis()
                            + SRUserManager.getInstance().getUser().uid + ".aac";
                    ZYUtils.uploadFile(SRTaskCommentActivity.this, key, filePath, SRUserManager.getInstance().getUser().upload_token, new CallBack() {
                        @Override
                        public void onProcess(long current, long total) {

                        }

                        @Override
                        public void onSuccess(UploadCallRet ret) {
                            hideProgress();
                            if (ret != null) {
                                try {
                                    mQiniuKey = ret.getKey();
                                    mDurationSe = durationSe;
                                    ZYLog.e(SRMarkFragment.class.getSimpleName(), "uploadAudio-key: " + mQiniuKey);
                                    layoutVoice.setVisibility(View.VISIBLE);
                                    layoutVoiceTip.setVisibility(View.VISIBLE);
                                    textVoiceSize.setText(ZYUtils.getShowHourMinuteSecond(mDurationSe));
                                    mFilePath = filePath;
                                } catch (Exception e) {
                                    ZYToast.show(SRTaskCommentActivity.this, e.getMessage() + "");
                                }
                            } else {
                                ZYToast.show(SRTaskCommentActivity.this, "上传失败,请重新录音,再重试");
                            }
                        }

                        @Override
                        public void onFailure(CallRet ret) {
                            hideProgress();
                            if (ret.getStatusCode() == 401) {
                                try {
                                    ZYToast.show(SRApplication.getInstance(), "登录信息失效,请重新登录");
                                    SRApplication.getInstance().getCurrentActivity().startActivity(SRLoginActivity.createIntent(SRApplication.getInstance().getCurrentActivity()));
                                } catch (Exception e) {
                                    ZYLog.e(getClass().getSimpleName(), "onNext:" + e.getMessage());
                                }
                            } else {
                                ZYToast.show(SRTaskCommentActivity.this, "上传失败: " + ret.getStatusCode());
                            }
                        }
                    });
                }
            }
        });
    }

    private String getTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void submitComment(final String comment, String comment_audio, String comment_audio_timelen) {
        showProgress();
        subscription.add(ZYNetSubscription.subscription(new SRTaskModel().addComment(mFinish, comment, comment_audio, comment_audio_timelen), new ZYNetSubscriber() {
            @Override
            public void onSuccess(ZYResponse response) {
                hideProgress();
                showToast("评论成功!");
                Intent intent = new Intent();
                intent.putExtra("comment", comment);
                setResult(100, intent);
                finish();
            }

            @Override
            public void onFail(String message) {
                hideProgress();
                super.onFail(message);
            }
        }));
    }

    @OnClick({R.id.layoutVoice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutVoice:
                if (ZYAudioPlayManager.getInstance().isStartPlay()) {
                    ZYAudioPlayManager.getInstance().startOrPuase();
                } else {
                    ZYAudioPlayManager.getInstance().play(mFilePath);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(ZYAudionPlayEvent playEvent) {
        if (playEvent != null) {
            if (mFilePath.equals(playEvent.url)) {
                if (playEvent.state == ZYAudioPlayManager.STATE_ERROR ||
                        playEvent.state == ZYAudioPlayManager.STATE_PAUSED ||
                        playEvent.state == ZYAudioPlayManager.STATE_COMPLETED ||
                        playEvent.state == ZYAudioPlayManager.STATE_STOP) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZYAudioPlayManager.getInstance().stop();
        subscription.unsubscribe();
    }
}
