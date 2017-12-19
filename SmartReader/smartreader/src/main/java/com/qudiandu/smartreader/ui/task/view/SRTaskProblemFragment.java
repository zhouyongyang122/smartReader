package com.qudiandu.smartreader.ui.task.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @Bind(R.id.imgBg)
    ImageView mImgBg;

    @Bind(R.id.textDesc)
    TextView mTextDesc;

    @Bind(R.id.layoutVoice)
    RelativeLayout mLayoutVoice;

    @Bind(R.id.imgVoice)
    ImageView mImgVoice;

    @Bind(R.id.layoutRecord)
    LinearLayout mLayoutRecord;

    @Bind(R.id.textRecord)
    ZYRecordAudioTextView mTextRecord;

    @Bind(R.id.layoutPre)
    LinearLayout mLayoutPre;

    @Bind(R.id.layoutNext)
    LinearLayout mLayoutNext;

    @Bind(R.id.textSubmit)
    TextView mTextSubmit;

    @Bind(R.id.viewSpace)
    View viewSpace;

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
            mTextSubmit.setVisibility(View.VISIBLE);
            String title = "提交";
            if (SRUserManager.getInstance().getUser().isTeacher()) {
                title = "完成";
            }
            mTextSubmit.setText(title);
            mHasSubmit = true;
            mLayoutNext.setVisibility(View.INVISIBLE);
        }

        if (!mHasPre) {
            mLayoutPre.setVisibility(View.INVISIBLE);
        }

        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, mImgAvatar, mPresenter.getTeacher().avatar, R.drawable.def_avatar, R.drawable.def_avatar);
        ZYImageLoadHelper.getImageLoader().loadRoundImage(this, mImgBg, mPresenter.getProblem().pic, R.drawable.img_default_pic, R.drawable.img_default_pic, 8);
        mTextDesc.setText(mPresenter.getProblem().description);
        if (!TextUtils.isEmpty(mPresenter.getProblem().audio)) {
            mLayoutVoice.setVisibility(View.VISIBLE);
        }
        if (mPresenter.getProblem().ctype == SRTask.TASK_TYPE_PIC) {
            viewSpace.setVisibility(View.GONE);
            mProblemPicVH = new SRTaskProblemPicVH(this);
            mProblemPicVH.attachTo(mLayoutContent);
            mProblemPicVH.updateView(mPresenter.getProblem(), 0);
            mLayoutRecord.setVisibility(View.INVISIBLE);
        } else {
            mProblemAudioVH = new SRTaskProblemAudioVH();
            mProblemAudioVH.attachTo(mLayoutContent);
            if (SRUserManager.getInstance().getUser().isTeacher()) {
                viewSpace.setVisibility(View.GONE);
                mLayoutRecord.setVisibility(View.INVISIBLE);
                mProblemAudioVH.updateView(new SRTaskAudio((int) mPresenter.getProblem().getAudioTime(), mPresenter.getProblem().user_answer), 0);
                return;
            }
            mTextRecord.setListener(new ZYRecordAudioTextView.RecordAudioViewListener() {
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
                                        viewSpace.setVisibility(View.GONE);
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
            mImgAvatar.postDelayed(this, 800);
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
        if (mIsGoNext) {
            return;
        }
        next();
    }

    @OnClick({R.id.layoutVoice, R.id.layoutNext, R.id.layoutPre, R.id.textSubmit})
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
            case R.id.layoutNext:
                mIsGoNext = true;
                mImgAvatar.removeCallbacks(this);
                next();
                break;
            case R.id.layoutPre:
                ((SRTaskProblemActivity) mActivity).pre();
                break;
            case R.id.textSubmit:
                ((SRTaskProblemActivity) mActivity).submit();
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
                    mImgVoice.setImageResource(R.drawable.qa_icon_play);
                } else {
                    mImgVoice.setImageResource(R.drawable.qa_icon_suspend);
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
