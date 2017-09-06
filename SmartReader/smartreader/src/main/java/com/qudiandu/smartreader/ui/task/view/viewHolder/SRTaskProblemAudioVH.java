package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskAudio;

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

    SRTaskAudio mData;

    @Override
    public void updateView(SRTaskAudio data, int position) {
        if (data != null) {
            show();
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, SRUserManager.getInstance().getUser().avatar, R.drawable.def_avatar, R.drawable.def_avatar);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_audio;
    }

    @OnClick({R.id.layoutVoice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutVoice:
                SRPlayManager.getInstance().startAudio(mData.audioPath);
                break;
        }
    }
}
