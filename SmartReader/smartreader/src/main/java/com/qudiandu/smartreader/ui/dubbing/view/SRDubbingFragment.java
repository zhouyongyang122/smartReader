package com.qudiandu.smartreader.ui.dubbing.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.view.ZYCircleProgressView;
import com.qudiandu.smartreader.ui.dubbing.contract.SRDubbingContract;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkBean;
import com.qudiandu.smartreader.ui.dubbing.view.viewHolder.SRDubbingWordVH;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/12/1.
 */

public class SRDubbingFragment extends ZYBaseFragment<SRDubbingContract.IPresenter> implements SRDubbingContract.IView {

    View rootView;

    @Bind(R.id.textEn)
    TextView textEn;//英语翻译

    @Bind(R.id.textCn)
    TextView textCn;//中文翻译

    @Bind(R.id.layoutRecording)
    RelativeLayout layoutRecording;//正在录音的布局
    @Bind(R.id.progressView)
    ZYCircleProgressView progressView;//录音进度视图
    @Bind(R.id.textProgress)
    TextView textProgress;//录音进度时间

    @Bind(R.id.layoutRecord)
    LinearLayout layoutRecord;//录音按钮

    @Bind(R.id.layoutPlayAudio)
    LinearLayout layoutPlayAudio;//播放原音

    @Bind(R.id.layoutPlayRecord)
    LinearLayout layoutPlayRecord;//播放录音

    @Bind(R.id.layoutShare)
    LinearLayout layoutShare;//分享

    @Bind(R.id.textScoreTip)
    TextView textScoreTip;//配音提示

    @Bind(R.id.textScore)
    TextView textScore;//部分

    @Bind(R.id.layoutScoreRoot)
    RelativeLayout layoutScoreRoot;//单词分数

    @Bind(R.id.textNum)
    TextView textNum;//页数

    SRDubbingWordVH dubbingWordVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sr_fragment_dubbing, container, false);
        ButterKnife.bind(this, rootView);

        textNum.setText(mPresenter.getPageId());
        dubbingWordVH = new SRDubbingWordVH();
        dubbingWordVH.attachTo(layoutScoreRoot);
        return rootView;
    }

    @Override
    public void uploadAudioSuc(SRMarkBean markBean) {

    }

    @Override
    public void uploadMergeAudioSuc(SRCatalogueResponse response) {

    }

    public View getRootView() {
        return rootView;
    }
}
