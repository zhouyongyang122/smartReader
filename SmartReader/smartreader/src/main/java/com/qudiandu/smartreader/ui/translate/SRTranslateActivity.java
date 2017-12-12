package com.qudiandu.smartreader.ui.translate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.thirdParty.translate.TranslateRequest;
import com.qudiandu.smartreader.thirdParty.translate.YouDaoBean;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYSystemUtils;
import com.qudiandu.smartreader.utils.ZYToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/12/10.
 */

public class SRTranslateActivity extends ZYBaseActivity {

    @Bind(R.id.editSearch)
    EditText editSearch;

    @Bind(R.id.textEnWord)
    TextView textEnWord;

    @Bind(R.id.textCnWord)
    TextView textCnWord;

    @Bind(R.id.textExplanation)
    TextView textExplanation;

    @Bind(R.id.textExample)
    TextView textExample;

    public static Intent createIntent(Context context) {
        return new Intent(context, SRTranslateActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_translate);
        ButterKnife.bind(this);

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                ZYLog.e(SRTranslateActivity.class.getSimpleName(), "onEditorAction: " + actionId);
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    search();
                    return true;
                }
                return false;
            }
        });

        textEnWord.setText("");
        textExplanation.setText("");
        textCnWord.setText("");
        textExample.setText("");
    }

    void search() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
        showWaitDialog("查询中...");
        String word = editSearch.getText().toString();
        if (TextUtils.isEmpty(word)) {
            ZYToast.show(SRTranslateActivity.this, "搜索不能为空!");
            return;
        }
        TranslateRequest.getRequest().translate(word, new TranslateRequest.TranslateRequestCallBack() {
            @Override
            public void translateCallBack(YouDaoBean translateBean, String errorMsg) {
                if (translateBean != null) {
                    textEnWord.setText(translateBean.query);
                    textExplanation.setText(translateBean.getPhonetic());
                    textCnWord.setText(translateBean.getExplains());
                    textExample.setText(translateBean.getExample());
                } else {
                    ZYToast.show(SRTranslateActivity.this, errorMsg == null ? "网络错误,请重试尝试!" : errorMsg);
                }
                hideWaitDialog();
            }
        });
    }

    @OnClick({R.id.btnSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                search();
                break;
        }
    }
}
