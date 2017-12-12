package com.qudiandu.smartreader.ui.wordStudy.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyWord;
import com.qudiandu.smartreader.ui.wordStudy.view.viewHolder.SRWordStudyListenInputWordVH;

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

    @Bind(R.id.editWords)
    EditText editWords;

    @Bind(R.id.btnNext)
    TextView btnNext;

    SRWordStudyWord mWord;

    char[] mValues;

    int mIndex;

    StringBuffer builder;

    ArrayList<SRWordStudyListenInputWordVH> inputWordVHS = new ArrayList<SRWordStudyListenInputWordVH>();

    View.OnKeyListener keyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_DEL
                    && event.getAction() == KeyEvent.ACTION_UP) {
                delTextValue();
                return true;
            }
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_word_sutdy_listen, container, false);
        ButterKnife.bind(this, view);

        inputWordVHS.clear();

        builder = new StringBuffer();
        int position = 0;
        for (char value : mValues) {
            SRWordStudyListenInputWordVH inputWordVH = new SRWordStudyListenInputWordVH(position, value + "");
            inputWordVHS.add(inputWordVH);
            inputWordVH.attachTo(layoutInputWord);
            position++;
        }

        editWords.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputWordVHS.size())});
        editWords.setOnKeyListener(keyListener);
        editWords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    return;
                }

                if (builder.length() < inputWordVHS.size()) {
                    builder.append(s.toString());
                    setTextValue();
                }
                s.delete(0, s.length());
            }
        });

        return view;
    }

    @OnClick({R.id.imgDel, R.id.imgPlay, R.id.btnNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgDel:
                delTextValue();
                break;
            case R.id.imgPlay:
                break;
            case R.id.btnNext:
                break;
        }
    }

    private void setTextValue() {

        String str = builder.toString();
        int len = str.length();

        if (len <= 6) {
            inputWordVHS.get(len - 1).updateView(builder.substring(len - 1, len), 0);
        }
        if (len == 6) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editWords.getWindowToken(), 0);
        }
    }

    private void delTextValue() {
        String str = builder.toString();
        int len = str.length();
        if (len == 0) {
            return;
        }
        if (len > 0 && len <= 6) {
            builder.delete(len - 1, len);
        }
        inputWordVHS.get(len - 1).updateView("", 0);
    }

    public void setWord(SRWordStudyWord word) {
        mWord = word;
        mValues = mWord.word.toCharArray();
    }
}
