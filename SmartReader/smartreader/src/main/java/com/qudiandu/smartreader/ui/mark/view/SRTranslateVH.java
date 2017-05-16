package com.qudiandu.smartreader.ui.mark.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.translate.TranslateRequest;
import com.qudiandu.smartreader.thirdParty.translate.YouDaoBean;
import com.qudiandu.smartreader.ui.main.model.SRPageManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/3.
 */

public class SRTranslateVH extends ZYBaseViewHolder<YouDaoBean> {

    @Bind(R.id.textWord)
    TextView textWord;

    @Bind(R.id.textExplanation)
    TextView textExplanation;

    @Bind(R.id.textSoundMark)
    TextView textSoundMark;

    @Bind(R.id.imgAudio)
    ImageView imgAudio;

    YouDaoBean mBean;

    @Override
    public void updateView(YouDaoBean data, int position) {
        if (data != null) {
            mBean = data;
            textWord.setText(data.query);
            textExplanation.setText(data.getExplains());
            textSoundMark.setText("/" + data.getPhonetic() + "/");
            textExplanation.setVisibility(View.VISIBLE);
            textSoundMark.setVisibility(View.VISIBLE);
            imgAudio.setVisibility(View.VISIBLE);
        } else {
            textWord.setText("正在查询中...");
            textExplanation.setVisibility(View.INVISIBLE);
            textSoundMark.setVisibility(View.INVISIBLE);
            imgAudio.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.imgAudio, R.id.layoutTranslate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutTranslate:
                hide();
                break;
            case R.id.imgAudio:
                if (mBean != null) {
                    SRPageManager.getInstance().startAudio("http://dict.youdao.com/dictvoice?audio=" + mBean.query + "&amp;type=" + 1);
                }
                break;
        }
    }

    public void attachTo(ViewGroup viewGroup) {
        bindView(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false));
        viewGroup.addView(getItemView());
    }

    public void hide() {
        if (mItemView != null) {
            TranslateRequest.getRequest().setRequestCallBack(null);
            SRPageManager.getInstance().stopAudio();
            mItemView.setVisibility(View.GONE);
        }
    }

    public void show(YouDaoBean bean) {
        mItemView.setVisibility(View.VISIBLE);
        updateView(bean, 0);
    }

    public boolean isVisibility() {
        if (mItemView == null) {
            return false;
        }
        return mItemView.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_translate_show;
    }
}
