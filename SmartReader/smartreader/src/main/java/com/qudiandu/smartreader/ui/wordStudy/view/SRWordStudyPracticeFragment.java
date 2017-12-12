package com.qudiandu.smartreader.ui.wordStudy.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyWord;
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;
import com.qudiandu.smartreader.utils.ZYToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/12/12.
 */

public class SRWordStudyPracticeFragment extends ZYBaseFragment {

    @Bind(R.id.layoutPic)
    LinearLayout layoutPic;
    @Bind(R.id.layoutPicA)
    RelativeLayout layoutPicA;
    @Bind(R.id.imgPicA)
    ImageView imgPicA;
    @Bind(R.id.layoutPicB)
    RelativeLayout layoutPicB;
    @Bind(R.id.imgPicB)
    ImageView imgPicB;
    @Bind(R.id.layoutPicC)
    RelativeLayout layoutPicC;
    @Bind(R.id.imgPicC)
    ImageView imgPicC;
    @Bind(R.id.layoutPicD)
    RelativeLayout layoutPicD;
    @Bind(R.id.imgPicD)
    ImageView imgPicD;

    @Bind(R.id.layoutText)
    LinearLayout layoutText;
    @Bind(R.id.layoutTextA)
    RelativeLayout layoutTextA;
    @Bind(R.id.textA)
    TextView textA;
    @Bind(R.id.layoutTextB)
    RelativeLayout layoutTextB;
    @Bind(R.id.textB)
    TextView textB;
    @Bind(R.id.layoutTextC)
    RelativeLayout layoutTextC;
    @Bind(R.id.textC)
    TextView textC;
    @Bind(R.id.layoutTextD)
    RelativeLayout layoutTextD;
    @Bind(R.id.textD)
    TextView textD;

    @Bind(R.id.textTip)
    TextView textTip;

    @Bind(R.id.textNext)
    TextView textNext;

    @Bind(R.id.textEn)
    TextView textEn;

    SRWordStudyWord mWord;

    WordStudyPracticeFragmentListener mListener;

    boolean mLastWord;

    boolean mIsPicType;

    boolean isSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_word_sutdy_practice, container, false);
        ButterKnife.bind(this, view);

        textEn.setText(mWord.word);
        if (mIsPicType) {
            layoutPic.setVisibility(View.VISIBLE);
            layoutText.setVisibility(View.GONE);

            if (mWord.pic_problem != null) {
                ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgPicA, mWord.pic_problem.A, ZYScreenUtils.dp2px(mActivity, 6));
                ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgPicB, mWord.pic_problem.B, ZYScreenUtils.dp2px(mActivity, 6));
                ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgPicC, mWord.pic_problem.C, ZYScreenUtils.dp2px(mActivity, 6));
                ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgPicD, mWord.pic_problem.D, ZYScreenUtils.dp2px(mActivity, 6));
            }
        } else {
            layoutPic.setVisibility(View.GONE);
            layoutText.setVisibility(View.VISIBLE);

            if (mWord.text_problem != null) {
                textA.setText(mWord.text_problem.A);
                textB.setText(mWord.text_problem.B);
                textC.setText(mWord.text_problem.C);
                textD.setText(mWord.text_problem.D);
            }
        }
        if (mLastWord) {
            textNext.setText("完成");
        }
        return view;
    }

    public void setWord(SRWordStudyWord word) {
        mWord = word;
    }

    public void setLastWord(boolean lastWord) {
        mLastWord = lastWord;
    }

    public void setIsPicType(boolean isPicType) {
        mIsPicType = isPicType;
    }

    @OnClick({R.id.imgPlay,
            R.id.layoutPicA,
            R.id.layoutPicB,
            R.id.layoutPicC,
            R.id.layoutPicD,
            R.id.layoutTextA,
            R.id.layoutTextB,
            R.id.layoutTextC,
            R.id.layoutTextD,
            R.id.textNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgPlay:
                SRPlayManager.getInstance().startAudio("http://dict.youdao.com/dictvoice?audio=" + mWord.word + "&amp;type=" + 1);
                break;
            case R.id.layoutPicA:
                selectPicAnswer("A");
                break;
            case R.id.layoutPicB:
                selectPicAnswer("B");
                break;
            case R.id.layoutPicC:
                selectPicAnswer("C");
                break;
            case R.id.layoutPicD:
                selectPicAnswer("D");
                break;
            case R.id.layoutTextA:
                selectTextAnswer("A");
                break;
            case R.id.layoutTextB:
                selectTextAnswer("B");
                break;
            case R.id.layoutTextC:
                selectTextAnswer("C");
                break;
            case R.id.layoutTextD:
                selectTextAnswer("D");
                break;
            case R.id.textNext:
                if (!isSelected) {
                    ZYToast.show(mActivity, "还没有选择答案哦!");
                    return;
                }
                if (mLastWord) {
                    if (mIsPicType) {
                        finish();
                        return;
                    }
                    mListener.onFinishedAnswer();
                } else {
                    mListener.onNextAnswer();
                }
                break;
        }
    }

    void selectPicAnswer(String answer) {
        boolean isRight = false;
        if (answer.equals("A")) {
            isSelected = true;
            if (answer.equals(mWord.pic_answer)) {
                isRight = true;
                layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
                isRight = false;
            }
            layoutPicB.setBackgroundResource(R.color.white);
            layoutPicC.setBackgroundResource(R.color.white);
            layoutPicD.setBackgroundResource(R.color.white);
        } else if (answer.equals("B")) {
            isSelected = true;
            if (answer.equals(mWord.pic_answer)) {
                isRight = true;
                layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
                isRight = false;
            }
            layoutPicA.setBackgroundResource(R.color.white);
            layoutPicC.setBackgroundResource(R.color.white);
            layoutPicD.setBackgroundResource(R.color.white);
        } else if (answer.equals("C")) {
            isSelected = true;
            if (answer.equals(mWord.pic_answer)) {
                isRight = true;
                layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
                isRight = false;
            }
            layoutPicB.setBackgroundResource(R.color.white);
            layoutPicA.setBackgroundResource(R.color.white);
            layoutPicD.setBackgroundResource(R.color.white);
        } else if (answer.equals("D")) {
            isSelected = true;
            if (answer.equals(mWord.pic_answer)) {
                isRight = true;
                layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
                isRight = false;
            }
            layoutPicB.setBackgroundResource(R.color.white);
            layoutPicC.setBackgroundResource(R.color.white);
            layoutPicA.setBackgroundResource(R.color.white);
        }

        if (isRight) {
            textTip.setText("回答正确");
            textTip.setTextColor(ZYResourceUtils.getColor(R.color.c5));
        } else {
            textTip.setText("回答错误");
            textTip.setTextColor(ZYResourceUtils.getColor(R.color.c3));
        }
    }

    void selectTextAnswer(String answer) {
        boolean isRight = false;
        if (answer.equals("A")) {
            isSelected = true;
            if (answer.equals(mWord.text_answer)) {
                isRight = true;
                layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                isRight = false;
                layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
            layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("B")) {
            isSelected = true;
            if (answer.equals(mWord.text_answer)) {
                isRight = true;
                layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                isRight = false;
                layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
            layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("C")) {
            isSelected = true;
            if (answer.equals(mWord.text_answer)) {
                isRight = true;
                layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                isRight = false;
                layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
            layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("D")) {
            isSelected = true;
            if (answer.equals(mWord.text_answer)) {
                isRight = true;
                layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                isRight = false;
                layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
            layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        }

        if (isRight) {
            textTip.setText("回答正确");
            textTip.setTextColor(ZYResourceUtils.getColor(R.color.c5));
        } else {
            textTip.setText("回答错误");
            textTip.setTextColor(ZYResourceUtils.getColor(R.color.c3));
        }
    }

    public void setListener(WordStudyPracticeFragmentListener listener) {
        mListener = listener;
    }

    public interface WordStudyPracticeFragmentListener {
        void onFinishedAnswer();

        void onNextAnswer();
    }
}
