package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.player.ZYAudioPlayManager;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskAudio;
import com.qudiandu.smartreader.utils.ZYFileUtils;
import com.qudiandu.smartreader.utils.ZYUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/9/4.
 */

public class SRTaskProblemAudioVH extends ZYBaseViewHolder<SRTaskAudio> {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textVoiceSize)
    TextView textVoiceSize;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    SRTaskAudio mData;

    @Override
    public void updateView(SRTaskAudio data, int position) {
        if (data != null) {
            show();
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, SRUserManager.getInstance().getUser().avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textVoiceSize.setText(ZYUtils.getShowHourMinuteSecond(mData.audioSize));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_audio;
    }

    public void showProgress(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public String getAudioUrl() {
        if (mData != null) {
            return mData.audioPath;
        }
        return "";
    }

    @OnClick({R.id.layoutVoice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutVoice:
                if (ZYAudioPlayManager.getInstance().isSamePlay(mData.audioPath) &&
                        ZYAudioPlayManager.getInstance().isStartPlay()) {
                    ZYAudioPlayManager.getInstance().startOrPuase();
                } else {
                    ZYAudioPlayManager.getInstance().play(mData.audioPath);
                }
                break;
        }
    }

    public void destory() {
        if (mData != null && mData.audioPath != null) {
            ZYFileUtils.delete(mData.audioPath);
        }
    }
}
