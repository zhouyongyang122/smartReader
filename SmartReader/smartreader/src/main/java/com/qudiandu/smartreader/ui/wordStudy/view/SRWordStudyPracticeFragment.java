package com.qudiandu.smartreader.ui.wordStudy.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYMediaPlayerTool;
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

    boolean mIsNext;

    String mSelectAnswer;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            SRPlayManager.getInstance().stopAudio();
            SRPlayManager.getInstance().startAudio("http://dict.youdao.com/dictvoice?audio=" + mWord.word + "&amp;type=" + 1);
        }
    }

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
                mSelectAnswer = "A";
                selectPicAnswer(mSelectAnswer);
                break;
            case R.id.layoutPicB:
                mSelectAnswer = "B";
                selectPicAnswer(mSelectAnswer);
                break;
            case R.id.layoutPicC:
                mSelectAnswer = "C";
                selectPicAnswer(mSelectAnswer);
                break;
            case R.id.layoutPicD:
                mSelectAnswer = "D";
                selectPicAnswer(mSelectAnswer);
                break;
            case R.id.layoutTextA:
                mSelectAnswer = "A";
                selectTextAnswer(mSelectAnswer);
                break;
            case R.id.layoutTextB:
                mSelectAnswer = "B";
                selectTextAnswer(mSelectAnswer);
                break;
            case R.id.layoutTextC:
                mSelectAnswer = "C";
                selectTextAnswer(mSelectAnswer);
                break;
            case R.id.layoutTextD:
                mSelectAnswer = "D";
                selectTextAnswer(mSelectAnswer);
                break;
            case R.id.textNext:
                if (TextUtils.isEmpty(mSelectAnswer)) {
                    ZYToast.show(mActivity, "还没有选择答案哦!");
                    return;
                }

                if (mIsNext) {
                    if (mLastWord) {
                        if (mIsPicType) {
                            finish();
                            return;
                        }
                        mListener.onFinishedAnswer();
                    } else {
                        mListener.onNextAnswer();
                    }
                } else {
                    if (mLastWord) {
                        textNext.setText("完成");
                    } else {
                        textNext.setText("下一题");
                    }
                    mIsNext = true;
                    String oAnSwer = mIsPicType ? mWord.pic_answer.toUpperCase() : mWord.text_answer.toUpperCase();
                    String strValue = "正确答案:" + oAnSwer + "  你的答案:" + mSelectAnswer;
                    ZYLog.e(getClass().getSimpleName(), "strValue:" + strValue);
                    SpannableString value = null;
                    if (mSelectAnswer.equals(oAnSwer)) {
                        value = new SpannableString(strValue);
                        int start = 5;
                        int end = 5 + 1;
                        ZYLog.e(getClass().getSimpleName(), "start-1: " + start + "   end-1: " + end);
                        value.setSpan(new ForegroundColorSpan(Color.parseColor("#00d365")), start, end,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        start = end + 7;
                        end = start + 1;
                        ZYLog.e(getClass().getSimpleName(), "start-2: " + start + "   end-2: " + end);
                        value.setSpan(new ForegroundColorSpan(Color.parseColor("#00d365")), start, end,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        ZYMediaPlayerTool.playSound(mActivity, R.raw.right, false);
                    } else {
                        int start = 5;
                        int end = 5 + 1;
                        ZYLog.e(getClass().getSimpleName(), "start-1: " + start + "   end-1: " + end);
                        value = new SpannableString(strValue);
                        value.setSpan(new ForegroundColorSpan(Color.parseColor("#00d365")), start, end,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        start = end + 7;
                        end = start + 1;
                        ZYLog.e(getClass().getSimpleName(), "start-2: " + start + "   end-2: " + end);
                        value.setSpan(new ForegroundColorSpan(Color.parseColor("#f25b6a")), start, end,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        ZYMediaPlayerTool.playSound(mActivity, R.raw.right, false);
                    }
                    textTip.setText(value);
                    if (mIsPicType) {
                        showPicAnswer(oAnSwer, mSelectAnswer);
                    } else {
                        showTextAnswer(oAnSwer, mSelectAnswer);
                    }
                }
                break;
        }
    }

    void showTextAnswer(String oAnswer, String selAnswer) {
        if (oAnswer.equals("A")) {
            layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
        } else if (oAnswer.equals("B")) {
            layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
        } else if (oAnswer.equals("C")) {
            layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
        } else if (oAnswer.equals("D")) {
            layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
        }

        if (selAnswer.equals("A")) {
            if (oAnswer.equals(selAnswer)) {
                layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
        } else if (selAnswer.equals("B")) {
            if (oAnswer.equals(selAnswer)) {
                layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
        } else if (selAnswer.equals("C")) {
            if (oAnswer.equals(selAnswer)) {
                layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
        } else if (selAnswer.equals("D")) {
            if (oAnswer.equals(selAnswer)) {
                layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
        }
    }

    void showPicAnswer(String oAnswer, String selAnswer) {
        if (oAnswer.equals("A")) {
            layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
        } else if (oAnswer.equals("B")) {
            layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
        } else if (oAnswer.equals("C")) {
            layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
        } else if (oAnswer.equals("D")) {
            layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
        }

        if (selAnswer.equals("A")) {
            if (oAnswer.equals(selAnswer)) {
                layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
        } else if (selAnswer.equals("B")) {
            if (oAnswer.equals(selAnswer)) {
                layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
        } else if (selAnswer.equals("C")) {
            if (oAnswer.equals(selAnswer)) {
                layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
        } else if (selAnswer.equals("D")) {
            if (oAnswer.equals(selAnswer)) {
                layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_white);
            } else {
                layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c3_white);
            }
        }
    }

    void selectPicAnswer(String answer) {
//        textTip.setText("请检查答案");
//        mIsNext = false;
//        textNext.setText("检查");
        if (answer.equals("A")) {
            layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_white);
            layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("B")) {
            layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_white);
            layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("C")) {
            layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_white);
            layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("D")) {
            layoutPicD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_white);
            layoutPicB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutPicC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutPicA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        }
    }

    void selectTextAnswer(String answer) {
//        textTip.setText("请检查答案");
//        mIsNext = false;
//        textNext.setText("检查");
        if (answer.equals("A")) {
            layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_white);
            layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("B")) {
            layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_white);
            layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("C")) {
            layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_white);
            layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
        } else if (answer.equals("D")) {
            layoutTextD.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_white);
            layoutTextB.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextC.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
            layoutTextA.setBackgroundResource(R.drawable.sr_bg_corner6dp_white_solid);
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
