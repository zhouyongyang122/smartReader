package com.qudiandu.smartdub.ui.mark.view;

import android.support.v4.content.ContextCompat;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.view.ZYCircleProgressView;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.thirdParty.xiansheng.XSBean;
import com.qudiandu.smartdub.thirdParty.xiansheng.XianShengSDK;
import com.qudiandu.smartdub.ui.main.model.SRPlayManager;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.ui.dubbing.model.bean.SRMarkBean;
import com.qudiandu.smartdub.utils.ZYLog;
import com.qudiandu.smartdub.utils.ZYToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkItemVH extends ZYBaseViewHolder<SRTract> implements XianShengSDK.MarkListener {

    @Bind(R.id.layoutMark)
    RelativeLayout layoutMark;

    @Bind(R.id.textEn)
    TextView textEn;

    @Bind(R.id.textCn)
    TextView textCn;

    @Bind(R.id.layoutRecording)
    RelativeLayout layoutRecording;

    @Bind(R.id.progressView)
    ZYCircleProgressView progressView;

    @Bind(R.id.layoutRecord)
    LinearLayout layoutRecord;

    @Bind(R.id.layoutScore)
    LinearLayout layoutScore;

    @Bind(R.id.layoutScoreBg)
    RelativeLayout layoutScoreBg;

    @Bind(R.id.textScore)
    TextView textScore;

    @Bind(R.id.layoutShare)
    LinearLayout layoutShare;

    @Bind(R.id.textProgress)
    TextView textProgress;

    @Bind(R.id.layoutDef)
    RelativeLayout layoutDef;

    @Bind(R.id.textDefScore)
    TextView textDefScore;

    @Bind(R.id.textDefEn)
    TextView textDefEn;

    @Bind(R.id.textDefCn)
    TextView textDefCn;

    public static TextView lastClickTextView;

    private MarkItemListener listener;

    private SRTract mData;

    private SRMarkBean markBean;

    private int mPosition;

    SRMarkHeaderVH mHeaderVH;

    public SRMarkItemVH(MarkItemListener listener, SRMarkHeaderVH headerVH) {
        this.listener = listener;
        mHeaderVH = headerVH;
    }

    @Override
    public void updateView(SRTract data, int position) {
        if (data != null) {
            mData = data;
            markBean = mData.getMarkBean();
            mPosition = position;
            if (data.isRecordType) {
                layoutDef.setVisibility(View.GONE);
                layoutMark.setVisibility(View.VISIBLE);
                textCn.setText(data.getTrack_genre());
                textScore.setText(markBean.score + "");

                textEn.setText(data.getTrack_txt(), TextView.BufferType.SPANNABLE);
                setTextViewClickableSpan(textEn);
                textEn.setMovementMethod(LinkMovementMethod.getInstance());

                if (markBean.score > 0) {
                    layoutMark.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_solid);
                    layoutScore.setVisibility(View.VISIBLE);
                    layoutShare.setVisibility(View.VISIBLE);
                    if (markBean.score >= 60) {
                        layoutScoreBg.setBackgroundResource(R.drawable.sr_bg_corner360_c9_solid);
                    } else {
                        layoutScoreBg.setBackgroundResource(R.drawable.sr_bg_corner360_c10_solid);
                    }
                    textScore.setText(markBean.score + "");
                    if (data.isRecording) {
                        layoutRecord.setVisibility(View.INVISIBLE);
                        layoutRecording.setVisibility(View.VISIBLE);
                    } else {
                        layoutRecord.setVisibility(View.VISIBLE);
                        layoutRecording.setVisibility(View.GONE);
                    }

                } else {
                    layoutMark.setBackgroundResource(R.drawable.sr_bg_corner6dp_c5_solid);
                    layoutScore.setVisibility(View.GONE);
                    layoutShare.setVisibility(View.GONE);
                }
                SRPlayManager.getInstance().startAudio(mData.getMp3Path(), mData.getAudioStart(), mData.getAudioEnd());
            } else {
                layoutDef.setVisibility(View.VISIBLE);
                layoutMark.setVisibility(View.GONE);
                textDefEn.setText(data.getTrack_txt());
                textDefCn.setText(data.getTrack_genre());
                if (markBean.score >= 60) {
                    textDefScore.setVisibility(View.VISIBLE);
                    textDefScore.setBackgroundResource(R.drawable.pass);
                    textDefScore.setText(markBean.score + "");
                } else if (markBean.score > 0 && markBean.score < 60) {
                    textDefScore.setVisibility(View.VISIBLE);
                    textDefScore.setBackgroundResource(R.drawable.unpass);
                    textDefScore.setText(markBean.score + "");
                } else {
                    textDefScore.setVisibility(View.GONE);
                }
            }
        }
    }

    @OnClick({R.id.layoutDef, R.id.layoutPlay, R.id.layoutRecord, R.id.layoutShare, R.id.layoutScore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutDef:
                mData.isRecordType = true;
                updateView(mData, mPosition);
                if (listener != null) {
                    listener.onShowMarkingItem(mData, mPosition);
                }
                break;
            case R.id.layoutPlay:
                SRPlayManager.getInstance().startAudio(mData.getMp3Path(), mData.getAudioStart(), mData.getAudioEnd());
                break;
            case R.id.layoutRecord:
                SRPlayManager.getInstance().stopAudio();
                layoutRecord.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRecord();
                    }
                }, 100);
                break;
            case R.id.layoutShare:
                if (listener != null) {
                    listener.audioUpload(mData);
                }
                break;
            case R.id.layoutScore:
                SRPlayManager.getInstance().startAudio(markBean.audioPath);
                break;
        }
    }

    private void startRecord() {
        XianShengSDK.getInstance().start(this, markBean.audioPath);
        layoutRecording.setVisibility(View.VISIBLE);
        layoutRecord.setVisibility(View.INVISIBLE);
        progressView.setProgress(0);
        textProgress.setText(mData.getTractTime() + "s");
        mData.isRecording = true;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_mark_item;
    }


    @Override
    public long xfRecordTime() {
        return (long) (mData.getTractTime() * 1000) + 300;
    }

    @Override
    public String xfMarkString() {
        String text = mData.getTrack_txt();
        if (text != null && text.length() > 170) {
            text = text.substring(0, 170);
        }
        return text;
    }

    @Override
    public void xfRecordStart() {

    }

    @Override
    public void xfRecordProgress(final long current, final long total) {

        textProgress.post(new Runnable() {
            @Override
            public void run() {
                float progress = (float) current / (float) total;
                if (progress >= 1) {
                    progress = 1.0f;
                }
                float totalTime = mData.getTractTime();

                String value = String.format("%.2fs", totalTime - progress * totalTime);
                textProgress.setText(value);

                progressView.setProgress((int) (current * 100 / total));
            }
        });
    }

    @Override
    public void xfRecordEnd(final String path) {
        markBean.audioPath = path;
    }

    @Override
    public void xfMarkStart() {
        if (listener != null) {
            listener.onMarkStart();
        }
    }

    @Override
    public void xfMarkEnd(XSBean bean, String jsonValue) {
        if (listener != null) {
            listener.onMarkEnd();
        }
        layoutScore.setVisibility(View.VISIBLE);
        layoutShare.setVisibility(View.VISIBLE);
        layoutRecording.setVisibility(View.GONE);
        layoutRecord.setVisibility(View.VISIBLE);
        markBean.score = bean.result.overall;
        markBean.jsonValue = jsonValue;
        markBean.setScoreBean(bean);

        if (markBean.score >= 60) {
            layoutScoreBg.setBackgroundResource(R.drawable.sr_bg_corner360_c9_solid);
        } else {
            layoutScoreBg.setBackgroundResource(R.drawable.sr_bg_corner360_c10_solid);
        }
        textScore.setText(bean.result.overall + "");
        mHeaderVH.updateView(markBean, 0);
        markBean.update();
    }

    @Override
    public void xfMarkError(String error) {
        if (listener != null) {
            listener.onMarkError(error);
        }
        layoutRecording.setVisibility(View.GONE);
        layoutRecord.setVisibility(View.VISIBLE);
    }

    private void setTextViewClickableSpan(TextView textView) {
        // 改变选中文本的高亮背景颜色
        textView.setHighlightColor(ContextCompat.getColor(mContext, R.color.c9));
        Spannable spans = (Spannable) textView.getText();
        Integer[] indices = getIndices(textView.getText().toString().trim()
                + " ", ' ');
        int lengthEnd = spans.toString().indexOf('\n');
        lengthEnd = lengthEnd > 0 ? lengthEnd : spans.length();
        int start = 0;
        int end = 0;

        for (int i = 0; i < indices.length; i++) {
            ClickableSpan clickSpan = getClickableSpan();
            // to cater last/only word
            end = indices[i] < lengthEnd ? indices[i] : lengthEnd;
            spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (end == lengthEnd) {
                return;
            }
            start = end + 1;
        }
    }

    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView textView = (TextView) widget;
                if (lastClickTextView != null && !lastClickTextView.equals(textView)) {
                    Selection.setSelection((Spannable) lastClickTextView.getText(), 0, 0);
                }
                lastClickTextView = textView;
                if (textView.getSelectionStart() < 0
                        || textView.getSelectionEnd() <= textView.getSelectionStart()) {
                    ZYLog.e(getClass().getSimpleName(), "getClickableSpan: start < 0  end <= start");
                    return;
                }
                String selString = textView
                        .getText()
                        .subSequence(textView.getSelectionStart(),
                                textView.getSelectionEnd()).toString();
                String wrod = deleteNonAlpha(selString);
                if (wrod == null || wrod.length() == 0) {
                    ZYToast.show(mContext, "没有找到相关单词");
                    return;
                }
                ZYLog.e(getClass().getSimpleName(), "ClickableSpan-onClick: " + wrod);
                //调用查询接口
                if (listener != null) {
                    listener.onTranslate(wrod);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(0xff897d72);
                ds.setUnderlineText(false);
            }
        };
    }

    public static Integer[] getIndices(String s, char c) {
        int pos = s.indexOf(c, 0);
        List<Integer> indices = new ArrayList<Integer>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c, pos + 1);
        }
        return (Integer[]) indices.toArray(new Integer[0]);
    }

    public static String deleteNonAlpha(String word) {
        String tmp = null;
        for (int i = 0; i < word.length(); i++) {
            if (Character.isLetter(word.charAt(i))) {
                tmp = word.substring(i);
                break;
            }
        }
        if (tmp == null) {
            return null;
        }
        for (int j = tmp.length() - 1; j >= 0; j--) {
            if (Character.isLetter(tmp.charAt(j))) {
                tmp = tmp.substring(0, j + 1);
                break;
            }
        }
        return tmp;
    }

    public interface MarkItemListener {

        void onShowMarkingItem(SRTract tract, int position);

        void onTranslate(String word);

        void onMarkStart();

        void onMarkEnd();

        void onMarkError(String msg);

        void audioUpload(SRTract tract);
    }

}
