package com.qudiandu.smartreader.ui.task.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.thirdparty.V;
import com.qiniu.rs.CallBack;
import com.qiniu.rs.CallRet;
import com.qiniu.rs.UploadCallRet;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.event.ZYAudionPlayEvent;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.player.ZYAudioPlayManager;
import com.qudiandu.smartreader.base.record.ZYRecordAudioTextView;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.mark.view.SRMarkFragment;
import com.qudiandu.smartreader.ui.task.activity.SRTaskProblemActivity;
import com.qudiandu.smartreader.ui.task.contract.SRTaskProblemContact;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskAudio;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskProblemAudioVH;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskProblemPicVH;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;
import com.qudiandu.smartreader.utils.ZYUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/9/3.
 */

public class SRTaskProblemFragment extends ZYBaseFragment<SRTaskProblemContact.IPresenter> implements SRTaskProblemContact.IView,
        SRTaskProblemPicVH.TaskProblemPicListener,
        Runnable {

    @Bind(R.id.layoutContent)
    LinearLayout mLayoutContent;

    @Bind(R.id.imgAvatar)
    ImageView mImgAvatar;

    @Bind(R.id.textName)
    TextView mTextName;

    @Bind(R.id.textTime)
    TextView mTextTime;

    @Bind(R.id.imgBg)
    ImageView mImgBg;

    @Bind(R.id.textDesc)
    TextView mTextDesc;

    @Bind(R.id.layoutVoice)
    RelativeLayout mLayoutVoice;

    @Bind(R.id.textVoiceSize)
    TextView mTextVoiceSize;

    @Bind(R.id.layoutAnswerTip)
    RelativeLayout mLayoutAnswerTip;

    @Bind(R.id.imgTip)
    ImageView mImgTip;

    @Bind(R.id.textTip)
    TextView mTextTip;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.textRecord)
    ZYRecordAudioTextView textRecord;

    @Bind(R.id.textPre)
    TextView textPre;

    @Bind(R.id.textNext)
    TextView textNext;

    SRTaskProblemPicVH mProblemPicVH;

    SRTaskProblemAudioVH mProblemAudioVH;

    boolean mHasPre;

    boolean mHasNext;

    boolean mHasSubmit;

    SRTaskFinish mTaskFinish;

    boolean mIsGoNext;

    public void init(boolean hasPre, boolean hasNext, SRTaskFinish taskFinish) {
        mHasPre = hasPre;
        mHasNext = hasNext;
        mTaskFinish = taskFinish;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_task_problem, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        if (!mHasNext) {
            String title = "提交";
            if (SRUserManager.getInstance().getUser().isTeacher()) {
                title = "完成";
            }
            textNext.setText(title);
            mHasSubmit = true;
        } else {
            textNext.setText("下一题");
        }

        if (!mHasPre) {
            textPre.setVisibility(View.GONE);
        }

        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, mImgAvatar, mPresenter.getTeacher().avatar, R.drawable.def_avatar, R.drawable.def_avatar);
        mTextName.setText(mPresenter.getTeacher().nickname);
        mTextTime.setText(mPresenter.getTeacher().getCreateTime());
        ZYImageLoadHelper.getImageLoader().loadRoundImage(this, mImgBg, mPresenter.getProblem().pic, R.drawable.def_bg, R.drawable.def_bg, 8);
        mTextDesc.setText(mPresenter.getProblem().description);
        if (!TextUtils.isEmpty(mPresenter.getProblem().audio)) {
            mLayoutVoice.setVisibility(View.VISIBLE);
            mTextVoiceSize.setText(ZYUtils.getShowHourMinuteSecond((int) mPresenter.getProblem().getAudioTime()));
        }
        if (mPresenter.getProblem().ctype == SRTask.TASK_TYPE_PIC) {
            mProblemPicVH = new SRTaskProblemPicVH(this);
            mProblemPicVH.attachTo(mLayoutContent);
            mProblemPicVH.updateView(mPresenter.getProblem(), 0);
        } else {
            mProblemAudioVH = new SRTaskProblemAudioVH();
            mProblemAudioVH.attachTo(mLayoutContent);
            if (SRUserManager.getInstance().getUser().isTeacher()) {
                mProblemAudioVH.updateView(new SRTaskAudio((int) mPresenter.getProblem().getAudioTime(), mPresenter.getProblem().user_answer), 0);
                return;
            }
            textRecord.setVisibility(View.VISIBLE);
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
                        ZYToast.show(mActivity, "音频文件丢失,请重新录音!");
                    } else {
                        showProgress();
                        String key = getTime() + File.separator + System.currentTimeMillis()
                                + SRUserManager.getInstance().getUser().uid + ".aac";
                        ZYUtils.uploadFile(mActivity, key, filePath, SRUserManager.getInstance().getUser().upload_token, new CallBack() {
                            @Override
                            public void onProcess(long current, long total) {

                            }

                            @Override
                            public void onSuccess(UploadCallRet ret) {
                                hideProgress();
                                if (ret != null) {
                                    try {
                                        String picKey = ret.getKey();
                                        ZYLog.e(SRMarkFragment.class.getSimpleName(), "uploadAudio-key: " + picKey);
                                        ((SRTaskProblemActivity) mActivity).addAnswer(mPresenter.getProblem().problem_id + "", picKey);
                                        mProblemAudioVH.updateView(new SRTaskAudio(durationSe, filePath), 0);
                                        mPresenter.setFinised(true);
                                    } catch (Exception e) {
                                        ZYToast.show(mActivity, e.getMessage() + "");
                                    }
                                } else {
                                    ZYToast.show(mActivity, "上传失败,请重新录音,再重试");
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
                                    ZYToast.show(mActivity, "上传失败: " + ret.getStatusCode());
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onAnswerSelect(String answer) {
        mPresenter.setFinised(true);
        ((SRTaskProblemActivity) mActivity).addAnswer(mPresenter.getProblem().problem_id + "", answer);
        if (!mHasSubmit) {
            mLayoutAnswerTip.postDelayed(this, 800);
        }
    }

//    void showAnswerTip(String answer) {
//        if (mLayoutAnswerTip != null) {
//            mLayoutAnswerTip.setVisibility(View.VISIBLE);
//            if (mPresenter.getProblem().answer.equals(answer)) {
//                mImgTip.setBackgroundResource(R.drawable.right);
//                mTextTip.setText("太棒了，回答正确!");
//            } else {
//                mImgTip.setBackgroundResource(R.drawable.worry);
//                mTextTip.setText("答题不仔细，回答错误!");
//            }
//            mLayoutAnswerTip.postDelayed(this, 1500);
//        }
//    }

    void hideAnswerTip() {
        if (mLayoutAnswerTip != null) {
            mLayoutAnswerTip.removeCallbacks(this);
            mLayoutAnswerTip.setVisibility(View.GONE);
        }
    }

    public void play() {
        mIsGoNext = false;
        ZYAudioPlayManager.getInstance().stop();
        if (SRUserManager.getInstance().getUser().isTeacher()) {
            if (mPresenter.getProblem().ctype == SRTask.TASK_TYPE_AUDIO) {
                ZYAudioPlayManager.getInstance().play(mPresenter.getProblem().user_answer);
            }
        } else {
            ZYAudioPlayManager.getInstance().play(mPresenter.getProblem().audio);
        }
    }


    @Override
    public void run() {
//        hideAnswerTip();
        if (mIsGoNext) {
            return;
        }
        next();
    }

    @OnClick({R.id.layoutVoice, R.id.layoutAnswerTip, R.id.textNext, R.id.textPre})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutVoice:
                if (ZYAudioPlayManager.getInstance().isSamePlay(mPresenter.getProblem().audio) &&
                        ZYAudioPlayManager.getInstance().isStartPlay()) {
                    ZYAudioPlayManager.getInstance().startOrPuase();
                } else {
                    ZYAudioPlayManager.getInstance().play(mPresenter.getProblem().audio);
                }
                break;
            case R.id.layoutAnswerTip:
                hideAnswerTip();
                break;
            case R.id.textNext:
                mIsGoNext = true;
                mLayoutAnswerTip.removeCallbacks(this);
                next();
                break;
            case R.id.textPre:
                ((SRTaskProblemActivity) mActivity).pre();
                break;
        }
    }

    void next() {
        if (mHasSubmit) {
            if (!SRUserManager.getInstance().getUser().isTeacher()) {
                if (!mPresenter.isFinised()) {
                    ZYToast.show(mActivity, "还没有完成当前的任务哦!");
                    return;
                }
            }
            ((SRTaskProblemActivity) mActivity).submit();
        } else {
            if (!SRUserManager.getInstance().getUser().isTeacher()) {
                if (!mPresenter.isFinised()) {
                    ZYToast.show(mActivity, "还没有完成当前的任务哦!");
                    return;
                }
            }
            ((SRTaskProblemActivity) mActivity).next();
        }
    }

    private String getTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(ZYAudionPlayEvent playEvent) {
        if (playEvent != null) {
            if (mPresenter.getProblem().audio.equals(playEvent.url)) {
                if (playEvent.state == ZYAudioPlayManager.STATE_ERROR ||
                        playEvent.state == ZYAudioPlayManager.STATE_PAUSED ||
                        playEvent.state == ZYAudioPlayManager.STATE_COMPLETED ||
                        playEvent.state == ZYAudioPlayManager.STATE_STOP) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            } else if (mProblemAudioVH != null && mProblemAudioVH.getAudioUrl().equals(playEvent.url)) {
                if (playEvent.state == ZYAudioPlayManager.STATE_ERROR ||
                        playEvent.state == ZYAudioPlayManager.STATE_PAUSED ||
                        playEvent.state == ZYAudioPlayManager.STATE_COMPLETED ||
                        playEvent.state == ZYAudioPlayManager.STATE_STOP) {
                    mProblemAudioVH.showProgress(false);
                } else {
                    mProblemAudioVH.showProgress(true);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProblemAudioVH != null) {
            mProblemAudioVH.destory();
        }
    }
}
