package com.qudiandu.smartreader.ui.wordStudy.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

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

        ZYBaseRecyclerAdapter<String> adapter = new ZYBaseRecyclerAdapter<String>(mKeys) {
            @Override
            public ZYBaseViewHolder<String> createViewHolder(int type) {
                return new SRWordStudyKeyVH();
            }
        };
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 7);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String key = mKeys.get(position);
                if (!TextUtils.isEmpty(key)) {
                    if (builder.length() < inputWordVHS.size()) {
                        builder.append(key);
                        setTextValue();
                    }
                }
            }
        });
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
                    if (editValue.equals(mWord.word)) {
                        textTip.setText("真棒,回答正确!");
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
        textTip.setVisibility(View.GONE);
        textAnswer.setText("请输入答案");
        mIsNext = false;
        btnNext.setText("检查");
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
        keys.add("A");
        keys.add("B");
        keys.add("C");
        keys.add("D");
        keys.add("E");
        keys.add("F");
        keys.add("G");
        keys.add("H");
        keys.add("I");
        keys.add("J");
        keys.add("K");
        keys.add("L");
        keys.add("M");
        keys.add("N");
        keys.add("O");
        keys.add("P");
        keys.add("Q");
        keys.add("R");
        keys.add("S");
        keys.add("T");
        keys.add("U");
        keys.add("V");
        keys.add("W");
        keys.add("X");
        keys.add("Y");
        keys.add("Z");
        return keys;
    }
}
