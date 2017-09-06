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
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.task.contract.SRTaskProblemContact;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskProblemAudioVH;
import com.qudiandu.smartreader.ui.task.view.viewHolder.SRTaskProblemPicVH;

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
    TextView textRecord;

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
            mTextVoiceSize.setText("1分10秒");
        }
        if (mPresenter.getProblem().ctype == SRTask.TASK_TYPE_PIC) {
            mProblemPicVH = new SRTaskProblemPicVH(this);
            mProblemPicVH.attachTo(mLayoutContent);
        } else {
            textRecord.setVisibility(View.VISIBLE);
            mProblemAudioVH = new SRTaskProblemAudioVH();
            mProblemAudioVH.attachTo(mLayoutContent);
        }
    }

    @Override
    public void onAnswerSelect(String answer) {
        mPresenter.setFinised(true);
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
}
