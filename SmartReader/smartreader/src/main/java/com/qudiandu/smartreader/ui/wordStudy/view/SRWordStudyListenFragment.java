package com.qudiandu.smartreader.ui.wordStudy.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyWord;
import com.qudiandu.smartreader.ui.wordStudy.view.viewHolder.SRWordStudyKeyVH;
import com.qudiandu.smartreader.ui.wordStudy.view.viewHolder.SRWordStudyListenInputWordVH;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYMediaPlayerTool;
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyListenFragment extends ZYBaseFragment {

    @Bind(R.id.layoutInputWord)
    LinearLayout layoutInputWord;

    @Bind(R.id.btnNext)
    TextView btnNext;

    @Bind(R.id.textTip)
    TextView textTip;

    @Bind(R.id.textAnswer)
    TextView textAnswer;

    @Bind(R.id.layoutWord1)
    LinearLayout layoutWord1;

    @Bind(R.id.layoutWord2)
    LinearLayout layoutWord2;

    @Bind(R.id.layoutWord3)
    LinearLayout layoutWord3;

    ArrayList<String> mKeys;

    SRWordStudyWord mWord;

    char[] mValues;

    StringBuffer builder;

    int mIndex;

    WordStudyListener mListener;

    boolean mIsLastIndex;

    boolean mIsNext;

    ArrayList<SRWordStudyListenInputWordVH> inputWordVHS = new ArrayList<SRWordStudyListenInputWordVH>();

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
        View view = inflater.inflate(R.layout.sr_fragment_word_sutdy_listen, container, false);
        ButterKnife.bind(this, view);

        inputWordVHS.clear();

        mKeys = keys();

        builder = new StringBuffer();
        int position = 0;
        for (char value : mValues) {
            SRWordStudyListenInputWordVH inputWordVH = new SRWordStudyListenInputWordVH(position, value + "");
            inputWordVHS.add(inputWordVH);
            inputWordVH.attachTo(layoutInputWord);
            position++;
        }

        if (mIsLastIndex) {
            btnNext.setText("完成");
        }

        SRWordStudyKeyVH keyVH;
        int index = 0;
        for (String key : mKeys) {
            if (key.equals("空格")) {
                keyVH = new SRWordStudyKeyVH(2, new SRWordStudyKeyVH.WordStudyKeyListener() {
                    @Override
                    public void onWordClick(int position) {
                        String key = mKeys.get(position);
                        if (!TextUtils.isEmpty(key)) {
                            if (key.equals("空格")) {
                                key = " ";
                            }
                            if (builder.length() < inputWordVHS.size()) {
                                builder.append(key);
                                setTextValue();
                            }
                        }
                    }
                });
            } else {
                keyVH = new SRWordStudyKeyVH(1, new SRWordStudyKeyVH.WordStudyKeyListener() {
                    @Override
                    public void onWordClick(int position) {
                        String key = mKeys.get(position);
                        if (!TextUtils.isEmpty(key)) {
                            if (key.equals("空格")) {
                                key = " ";
                            }
                            if (builder.length() < inputWordVHS.size()) {
                                builder.append(key);
                                setTextValue();
                            }
                        }
                    }
                });
            }
            if (index >= 0 && index <= 9) {
                keyVH.attachTo(layoutWord1);
            } else if (index > 9 && index <= 18) {
                keyVH.attachTo(layoutWord2);
            } else {
                keyVH.attachTo(layoutWord3);
            }
            keyVH.updateView(key, index);
            index++;
        }
        return view;
    }

    @OnClick({R.id.imgDel, R.id.imgPlay, R.id.btnNext, R.id.layoutInputWord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgDel:
                delTextValue();
                break;
            case R.id.imgPlay:
                SRPlayManager.getInstance().startAudio("http://dict.youdao.com/dictvoice?audio=" + mWord.word + "&amp;type=" + 1);
                break;
            case R.id.btnNext:
                if (mIsNext) {
                    if (mIsLastIndex) {
                        mListener.onFinished();
                        return;
                    }
                    mListener.onNext();
                } else {
                    String editValue = builder.toString();
                    if (editValue.length() < mWord.word.length()) {
                        ZYToast.show(mActivity, "还没有输入完整哦!");
                        return;
                    }
                    mIsNext = true;
                    btnNext.setText("下一题");
                    textTip.setVisibility(View.VISIBLE);
                    String strValue = "正确答案:" + mWord.word.toLowerCase() + "  你的答案:" + editValue.toLowerCase();
                    ZYLog.e(getClass().getSimpleName(), "strValue:" + strValue);
                    SpannableString value = null;
                    if (editValue.toLowerCase().equals(mWord.word.toLowerCase())) {
                        textTip.setText("真棒,回答正确!");
                        ZYMediaPlayerTool.playSound(mActivity, R.raw.right, false);
                        textTip.setTextColor(ZYResourceUtils.getColor(R.color.c5));
                        value = new SpannableString(strValue);
                        int start = 5;
                        int end = 5 + mWord.word.length();
                        ZYLog.e(getClass().getSimpleName(), "start-1: " + start + "   end-1: " + end);
                        value.setSpan(new ForegroundColorSpan(Color.parseColor("#00d365")), start, end,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        start = end + 7;
                        end = start + editValue.length();
                        ZYLog.e(getClass().getSimpleName(), "start-2: " + start + "   end-2: " + end);
                        value.setSpan(new ForegroundColorSpan(Color.parseColor("#00d365")), start, end,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        textTip.setText("哇哦,回答错误!");
                        ZYMediaPlayerTool.playSound(mActivity, R.raw.right, false);
                        textTip.setTextColor(ZYResourceUtils.getColor(R.color.c3));
                        int start = 5;
                        int end = 5 + mWord.word.length();
                        ZYLog.e(getClass().getSimpleName(), "start-1: " + start + "   end-1: " + end);
                        value = new SpannableString(strValue);
                        value.setSpan(new ForegroundColorSpan(Color.parseColor("#00d365")), start, end,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        start = end + 7;
                        end = start + editValue.length();
                        ZYLog.e(getClass().getSimpleName(), "start-2: " + start + "   end-2: " + end);
                        value.setSpan(new ForegroundColorSpan(Color.parseColor("#f25b6a")), start, end,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    textAnswer.setText(value);
                }
                break;
        }
    }

    private void setTextValue() {

        String str = builder.toString();
        int len = str.length();

        if (len <= inputWordVHS.size()) {
            inputWordVHS.get(len - 1).updateView(builder.substring(len - 1, len), 0);
        }
    }

    private void delTextValue() {
        String str = builder.toString();
        int len = str.length();
        if (len == 0) {
            return;
        }
        if (len > 0 && len <= inputWordVHS.size()) {
            builder.delete(len - 1, len);
        }
        inputWordVHS.get(len - 1).updateView("", 0);
//        textTip.setVisibility(View.GONE);
//        textAnswer.setText("请输入答案");
//        mIsNext = false;
//        btnNext.setText("检查");
    }

    public void setWord(SRWordStudyWord word, int index) {
        mWord = word;
        mValues = mWord.word.toCharArray();
        mIndex = index;
    }

    public void setListener(WordStudyListener listener) {
        mListener = listener;
    }

    public void setIsLastIndex(boolean isLastIndex) {
        this.mIsLastIndex = isLastIndex;
    }

    public interface WordStudyListener {
        void onNext();

        void onFinished();
    }

    ArrayList<String> keys() {
        ArrayList<String> keys = new ArrayList<String>();
        keys.add("q");
        keys.add("w");
        keys.add("e");
        keys.add("r");
        keys.add("t");
        keys.add("y");
        keys.add("u");
        keys.add("i");
        keys.add("o");
        keys.add("p");
        keys.add("a");
        keys.add("s");
        keys.add("d");
        keys.add("f");
        keys.add("g");
        keys.add("h");
        keys.add("j");
        keys.add("k");
        keys.add("l");
        keys.add("z");
        keys.add("x");
        keys.add("c");
        keys.add("v");
        keys.add("b");
        keys.add("n");
        keys.add("m");
        keys.add("空格");
        return keys;
    }
}
