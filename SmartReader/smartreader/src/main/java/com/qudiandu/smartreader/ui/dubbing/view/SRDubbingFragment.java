package com.qudiandu.smartreader.ui.dubbing.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiniu.rs.CallBack;
import com.qiniu.rs.CallRet;
import com.qiniu.rs.UploadCallRet;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.view.ZYCircleProgressView;
import com.qudiandu.smartreader.thirdParty.image.ZYIImageLoader;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.thirdParty.xiansheng.XSBean;
import com.qudiandu.smartreader.thirdParty.xiansheng.XianShengSDK;
import com.qudiandu.smartreader.thirdParty.xunfei.XunFeiSDK;
import com.qudiandu.smartreader.ui.dubbing.contract.SRDubbingContract;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkBean;
import com.qudiandu.smartreader.ui.dubbing.view.viewHolder.SRDubbingWordVH;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.view.SRMarkFragment;
import com.qudiandu.smartreader.utils.SRShareUtils;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;
import com.qudiandu.smartreader.utils.ZYUtils;
import com.third.loginshare.entity.ShareEntity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/12/1.
 */

public class SRDubbingFragment extends ZYBaseFragment<SRDubbingContract.IPresenter> implements SRDubbingContract.IView,
        XunFeiSDK.MarkListener {

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

    @Bind(R.id.layoutPlayRecord)
    LinearLayout layoutPlayRecord;

    @Bind(R.id.layoutShare)
    LinearLayout layoutShare;//分享

    @Bind(R.id.textScoreTip)
    TextView textScoreTip;//配音提示

    @Bind(R.id.textScore)
    TextView textScore;//分数

    @Bind(R.id.layoutScoreRoot)
    RelativeLayout layoutScoreRoot;//单词分数

    @Bind(R.id.textNum)
    TextView textNum;//页数

    public static TextView lastClickTextView;

    SRDubbingWordVH dubbingWordVH;

    TranslateListener mTranslateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sr_fragment_dubbing, container, false);
        ButterKnife.bind(this, rootView);

        textNum.setText((mPresenter.getPageId() + 1) + "");
        dubbingWordVH = new SRDubbingWordVH();
        dubbingWordVH.attachTo(layoutScoreRoot);

        SRTract mTract = mPresenter.getTrack();
        textEn.setText(mTract.getTrack_txt(), TextView.BufferType.SPANNABLE);
        setTextViewClickableSpan(textEn);
        textEn.setMovementMethod(LinkMovementMethod.getInstance());
        textCn.setText(mTract.getTrack_genre());

        refreshView();

        return rootView;
    }

    private void refreshView() {
        SRMarkBean mMart = mPresenter.getTrack().getMarkBean();
        layoutRecord.setVisibility(View.VISIBLE);
        layoutRecording.setVisibility(View.INVISIBLE);
        if (mMart.score > 0) {
            layoutShare.setVisibility(View.VISIBLE);
            layoutPlayRecord.setVisibility(View.VISIBLE);
            textScoreTip.setVisibility(View.INVISIBLE);
            textScore.setVisibility(View.VISIBLE);
            SpannableString spanText = new SpannableString("总分 " + mMart.score);
            spanText.setSpan(new AbsoluteSizeSpan(28, true), 3, spanText.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            if (mMart.score >= 60) {
                spanText.setSpan(new ForegroundColorSpan(Color.parseColor("#2ea7fc")), 3, spanText.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                textScore.setText(spanText);
            } else {
                spanText.setSpan(new ForegroundColorSpan(Color.parseColor("#f25b6a")), 3, spanText.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                textScore.setText(spanText);
            }
        } else {
            textScoreTip.setVisibility(View.VISIBLE);
            textScore.setVisibility(View.INVISIBLE);
            layoutPlayRecord.setVisibility(View.GONE);
            layoutShare.setVisibility(View.GONE);
        }

        dubbingWordVH.updateView(mMart, 0);
    }

    private void startRecord() {
        XianShengSDK.getInstance().start(this, mPresenter.getTrack().getMarkBean().audioPath);
        layoutRecording.setVisibility(View.VISIBLE);
        layoutRecord.setVisibility(View.INVISIBLE);
        progressView.setProgress(0);
        textProgress.setText(mPresenter.getTrack().getTractTime() + "s");
    }

    @OnClick({R.id.layoutRecord, R.id.layoutPlayAudio, R.id.layoutPlayRecord, R.id.layoutShare})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutPlayAudio:
                SRPlayManager.getInstance().startAudio(mPresenter.getTrack().getMp3Path(), mPresenter.getTrack().getAudioStart(), mPresenter.getTrack().getAudioEnd());
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
                audioUpload(mPresenter.getTrack());
                break;
            case R.id.layoutPlayRecord:
                SRPlayManager.getInstance().startAudio(mPresenter.getTrack().getMarkBean().audioPath);
                break;
        }
    }

    @Override
    public void uploadAudioSuc(SRMarkBean markBean) {
        share(markBean);
    }

    @Override
    public void uploadMergeAudioSuc(SRCatalogueResponse response) {

    }

    @Override
    public long xfRecordTime() {
        return (long) (mPresenter.getTrack().getTractTime() * 1000) + 300;
    }

    @Override
    public String xfMarkString() {
        String text = mPresenter.getTrack().getTrack_txt();
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
                float totalTime = mPresenter.getTrack().getTractTime();

                String value = String.format("%.2fs", totalTime - progress * totalTime);
                textProgress.setText(value);

                progressView.setProgress((int) (current * 100 / total));
            }
        });
    }

    @Override
    public void xfRecordEnd(String path) {
        mPresenter.getTrack().getMarkBean().audioPath = path;
    }

    @Override
    public void xfMarkStart() {
        showWaitDialog("正在打分中....");
    }

    @Override
    public void xfMarkEnd(XSBean bean, String jsonValue) {
        hideWaitDialog();
        mPresenter.getTrack().getMarkBean().score = bean.result.overall;
        mPresenter.getTrack().getMarkBean().jsonValue = jsonValue;
        mPresenter.getTrack().getMarkBean().setScoreBean(bean);
        refreshView();
        mPresenter.getTrack().getMarkBean().update();
    }

    @Override
    public void xfMarkError(String error) {
        ZYToast.show(mActivity, error);
        hide();
    }

    private void setTextViewClickableSpan(TextView textView) {
        // 改变选中文本的高亮背景颜色
        textView.setHighlightColor(ContextCompat.getColor(mActivity, R.color.c9));
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
                    ZYToast.show(mActivity, "没有找到相关单词");
                    return;
                }
                ZYLog.e(getClass().getSimpleName(), "ClickableSpan-onClick: " + wrod);
                //调用查询接口
                if (mTranslateListener != null) {
                    mTranslateListener.onTranslate(wrod);
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

    public View getRootView() {
        return rootView;
    }

    public void setTranslateListener(TranslateListener translateListener) {
        mTranslateListener = translateListener;
    }

    public void show() {
        refreshView();
    }

    public void hide() {
        layoutRecord.setVisibility(View.VISIBLE);
        layoutRecording.setVisibility(View.INVISIBLE);
    }

    public void audioUpload(final SRTract tract) {
        SRMarkBean markBean = tract.getMarkBean();
        if (markBean.share_url != null) {
            //已经上传成功,直接显示分享
            share(markBean);
        } else if (tract.audioQiNiuKey != null) {
            //音频已经上传成功,上传信息到服务器
            mPresenter.uploadTractAudio();
        } else {
            //先上传音频到七牛
            File file = new File(markBean.audioPath);
            if (!file.exists()) {
                ZYToast.show(mActivity, "音频文件丢失,请重新录音!");
            } else {
                showProgress();

                String key = getTime() + File.separator + System.currentTimeMillis()
                        + SRUserManager.getInstance().getUser().uid + ".wav";

                ZYUtils.uploadFile(mActivity, key, markBean.audioPath, SRUserManager.getInstance().getUser().upload_token, new CallBack() {
                    @Override
                    public void onProcess(long current, long total) {

                    }

                    @Override
                    public void onSuccess(UploadCallRet ret) {
                        if (ret != null) {
                            try {
                                String picKey = ret.getKey();
                                ZYLog.e(SRMarkFragment.class.getSimpleName(), "uploadAudio-key: " + picKey);
                                tract.audioQiNiuKey = picKey;
                                mPresenter.uploadTractAudio();
                            } catch (Exception e) {
                                hideProgress();
                                ZYToast.show(mActivity, e.getMessage() + "");
                            }
                        } else {
                            hideProgress();
                            ZYToast.show(mActivity, "上传失败,请重新录音,再重试");
                        }
                    }

                    @Override
                    public void onFailure(CallRet ret) {
                        hideProgress();
                        if (ret.getStatusCode() == 401) {
                            try {
                                ZYToast.show(SRApplication.getInstance(), "登录信息失效,请重新登录");
                                SRApplication.getInstance().getCurrentActivity().startActivity(SRLoginActivity.createIntent(SRApplication.getInstance().getCurrentActivity()));
                            } catch (Exception e) {
                                ZYLog.e(getClass().getSimpleName(), "onNext:" + e.getMessage());
                            }
                        } else {
                            ZYToast.show(mActivity, "上传失败: " + ret.getStatusCode());
                        }
                    }
                });
            }
        }
    }

    private void share(final SRMarkBean markBean) {

        ZYImageLoadHelper.getImageLoader().loadFromMediaStore(this, SRUserManager.getInstance().getUser().avatar, new ZYIImageLoader.OnLoadLocalImageFinishListener() {
            @Override
            public void onLoadFinish(@Nullable final Bitmap bitmap) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShareEntity shareEntity = new ShareEntity();
                        shareEntity.avatarUrl = SRUserManager.getInstance().getUser().avatar;
                        if (bitmap != null) {
                            shareEntity.avatarBitmap = bitmap;
                        } else {
                            shareEntity.avatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        }
                        shareEntity.webUrl = markBean.share_url;
                        shareEntity.title = SRUserManager.getInstance().getUser().nickname + "的录音作品快来听一下吧!";
                        shareEntity.text = "专为小学生设计的智能学习机";
                        new SRShareUtils(mActivity, shareEntity).share();
                    }
                });
            }
        });
    }

    private String getTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public interface TranslateListener {
        void onTranslate(String word);
    }
}
