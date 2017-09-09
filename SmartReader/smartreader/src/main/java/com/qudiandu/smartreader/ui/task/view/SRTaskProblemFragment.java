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

import com.iflytek.cloud.thirdparty.V;
import com.qiniu.rs.CallBack;
import com.qiniu.rs.CallRet;
import com.qiniu.rs.UploadCallRet;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.record.ZYRecordAudioTextView;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.mark.view.SRMarkFragment;
import com.qudiandu.smartreader.ui.task.activity.SRTaskProblemActivity;
import com.qudiandu.smartreader.ui.task.contract.SRTaskProblemContact;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskAudio;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskProblemAudioVH;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskProblemPicVH;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;
import com.qudiandu.smartreader.utils.ZYUtils;

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
        SRTaskProblemPicVH.TaskProblemPicListener {

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

    @Bind(R.id.textRecord)
    ZYRecordAudioTextView textRecord;

    SRTaskProblemPicVH mProblemPicVH;

    SRTaskProblemAudioVH mProblemAudioVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_task_problem, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, mImgAvatar, mPresenter.getTeacher().avatar, R.drawable.def_avatar, R.drawable.def_avatar);
        mTextName.setText(mPresenter.getTeacher().nickname);
        mTextTime.setText(mPresenter.getTeacher().getCreateTime());
        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, mImgBg, mPresenter.getProblem().pic, R.drawable.def_bg, R.drawable.def_bg);
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
            textRecord.setVisibility(View.VISIBLE);
            mProblemAudioVH = new SRTaskProblemAudioVH();
            mProblemAudioVH.attachTo(mLayoutContent);

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
                                        textRecord.setVisibility(View.GONE);
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
        mLayoutAnswerTip.setVisibility(View.VISIBLE);
        if (mPresenter.getProblem().answer.equals(answer)) {
            mImgTip.setBackgroundResource(R.drawable.right);
            mTextTip.setText("太棒了，回答正确!");
        } else {
            mImgTip.setBackgroundResource(R.drawable.worry);
            mTextTip.setText("答题不仔细，回答错误!");
        }
    }

    @OnClick({R.id.layoutVoice, R.id.layoutAnswerTip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutVoice:
                SRPlayManager.getInstance().startAudio(mPresenter.getProblem().audio);
                break;
            case R.id.layoutAnswerTip:
                mLayoutAnswerTip.setVisibility(View.GONE);
                break;
        }
    }

    private String getTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProblemAudioVH != null) {
            mProblemAudioVH.destory();
        }
    }
}
