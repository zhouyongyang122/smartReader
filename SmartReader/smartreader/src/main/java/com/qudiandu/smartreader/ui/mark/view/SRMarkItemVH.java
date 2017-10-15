package com.qudiandu.smartreader.ui.mark.view;

import android.support.v4.content.ContextCompat;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.xiansheng.XSBean;
import com.qudiandu.smartreader.thirdParty.xiansheng.XianShengSDK;
import com.qudiandu.smartreader.thirdParty.xunfei.XunFeiSDK;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkItemVH extends ZYBaseViewHolder<SRTract> implements XunFeiSDK.MarkListener {

    @Bind(R.id.layoutMark)
    RelativeLayout layoutMark;

    @Bind(R.id.textEn)
    TextView textEn;

    @Bind(R.id.textCn)
    TextView textCn;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.imgPlay)
    ImageView imgPlay;

    @Bind(R.id.imgRecord)
    ImageView imgRecord;

    @Bind(R.id.layoutScore)
    RelativeLayout layoutScore;

    @Bind(R.id.textScore)
    TextView textScore;

    @Bind(R.id.imgShare)
    ImageView imgShare;

    @Bind(R.id.layoutProgressBar)
    RelativeLayout layoutProgressBar;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.textProgress)
    TextView textProgress;

    @Bind(R.id.layoutDef)
    RelativeLayout layoutDef;

    @Bind(R.id.textDefScore)
    TextView textDefScore;

    @Bind(R.id.textDef)
    TextView textDef;

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
                textEn.setText(data.getTrack_txt(), TextView.BufferType.SPANNABLE);
                textCn.setText(data.getTrack_genre());
                textTime.setText(data.getTractTime() + "s");
                textScore.setText(markBean.score + "");

                setTextViewClickableSpan(textEn);
                textEn.setMovementMethod(LinkMovementMethod.getInstance());

                if (markBean.score > 0) {
                    if (data.isRecording) {
                        layoutScore.setVisibility(View.GONE);
                        if (markBean.score >= 60) {
                            layoutScore.setBackgroundResource(R.drawable.sr_bg_corner360_c9_solid);
                        } else {
                            layoutScore.setBackgroundResource(R.drawable.sr_bg_corner360_c10_solid);
                        }
                        textScore.setText(markBean.score + "");
                        imgShare.setVisibility(View.GONE);
                        layoutProgressBar.setVisibility(View.VISIBLE);
                    } else {
                        layoutScore.setVisibility(View.VISIBLE);
                        imgShare.setVisibility(View.VISIBLE);
                        layoutProgressBar.setVisibility(View.GONE);
                    }

                } else {
                    layoutScore.setVisibility(View.GONE);
                    imgShare.setVisibility(View.GONE);
                    layoutProgressBar.setVisibility(View.GONE);
                }

                SRPlayManager.getInstance().startAudio(mData.getMp3Path(), mData.getAudioStart(), mData.getAudioEnd());
            } else {
                layoutDef.setVisibility(View.VISIBLE);
                layoutMark.setVisibility(View.GONE);
                textDef.setText(data.getTrack_txt());

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

    @OnClick({R.id.textDef, R.id.imgPlay, R.id.imgRecord, R.id.imgShare, R.id.layoutScore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textDef:
                mData.isRecordType = true;
                updateView(mData, mPosition);
                if (listener != null) {
                    listener.onShowMarkingItem(mData, mPosition);
                }
                break;
            case R.id.imgPlay:
                SRPlayManager.getInstance().startAudio(mData.getMp3Path(), mData.getAudioStart(), mData.getAudioEnd());
                break;
            case R.id.imgRecord:
                SRPlayManager.getInstance().stopAudio();
                imgRecord.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRecord();
                    }
                }, 100);
                break;
            case R.id.imgShare:
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
        layoutProgressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        textProgress.setText("0s");
        layoutScore.setVisibility(View.GONE);
        mData.isRecording = true;
        imgShare.setVisibility(View.GONE);
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

                String value = String.format("%.2fs", progress * totalTime);
                textProgress.setText(value);

                progressBar.setProgress((int) (current * 100 / total));
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
        imgShare.setVisibility(View.VISIBLE);
        layoutProgressBar.setVisibility(View.GONE);
        markBean.score = bean.result.overall;
        markBean.jsonValue = jsonValue;
        markBean.setScoreBean(bean);

        if (markBean.score >= 60) {
            layoutScore.setBackgroundResource(R.drawable.sr_bg_corner360_c9_solid);
        } else {
            layoutScore.setBackgroundResource(R.drawable.sr_bg_corner360_c10_solid);
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
        layoutScore.setVisibility(View.VISIBLE);
        imgShare.setVisibility(View.VISIBLE);
        layoutProgressBar.setVisibility(View.GONE);
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
                ds.setColor(0xff2b2b2b);
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
