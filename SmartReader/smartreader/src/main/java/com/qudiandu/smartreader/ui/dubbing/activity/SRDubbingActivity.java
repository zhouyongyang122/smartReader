package com.qudiandu.smartreader.ui.dubbing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiniu.rs.CallBack;
import com.qiniu.rs.CallRet;
import com.qiniu.rs.UploadCallRet;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.translate.TranslateRequest;
import com.qudiandu.smartreader.thirdParty.translate.YouDaoBean;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkBean;
import com.qudiandu.smartreader.ui.dubbing.presenter.SRDubbingPresenter;
import com.qudiandu.smartreader.ui.dubbing.view.SRDubbingFragment;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.model.SRMarkModel;
import com.qudiandu.smartreader.ui.mark.presenter.SRMarkPresenter;
import com.qudiandu.smartreader.ui.mark.view.SRMarkFragment;
import com.qudiandu.smartreader.ui.mark.view.SRMarkShareVH;
import com.qudiandu.smartreader.ui.mark.view.SRTranslateVH;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYScreenUtils;
import com.qudiandu.smartreader.utils.ZYToast;
import com.qudiandu.smartreader.utils.ZYUtils;
import com.qudiandu.smartreader.utils.ZYWavMergeUtils;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/12/1.
 */

public class SRDubbingActivity extends ZYBaseActivity implements SRDubbingFragment.TranslateListener {

    static final String TRACTS = "tracts";
    static final String BOOK_ID = "book_id";
    static final String CATALOGUE_ID = "catalogue_id";
    static final String GROUP_ID = "group_id";
    static final String TASK_DI = "task_id";
    static final String TITLE = "title";

    public String mergeTractAudioPath;

    CompositeSubscription mSubscriptions = new CompositeSubscription();

    public static Intent createIntent(Context context
            , ArrayList<SRTract> tracts
            , String bookId
            , String catalogue_id
            , String title) {
        Intent intent = new Intent(context, SRDubbingActivity.class);
        intent.putExtra(TRACTS, tracts);
        intent.putExtra(BOOK_ID, bookId);
        intent.putExtra(CATALOGUE_ID, catalogue_id);
        intent.putExtra(TITLE, title);
        return intent;
    }

    public static Intent createIntent(Context context
            , ArrayList<SRTract> tracts
            , String bookId
            , String catalogue_id
            , String group_id
            , String task_id
            , String title) {
        Intent intent = new Intent(context, SRDubbingActivity.class);
        intent.putExtra(TRACTS, tracts);
        intent.putExtra(BOOK_ID, bookId);
        intent.putExtra(CATALOGUE_ID, catalogue_id);
        intent.putExtra(GROUP_ID, group_id);
        intent.putExtra(TASK_DI, task_id);
        intent.putExtra(TITLE, title);
        return intent;
    }

    @Bind(R.id.viewPage)
    ViewPager mViewPage;

    @Bind(R.id.textPage)
    TextView mTextPage;

    SRMarkShareVH markShareVH;

    @Bind(R.id.layoutSubmit)
    RelativeLayout layoutSubmit;

    DubbingAdapter mAdapter;

    List<SRDubbingFragment> mFragments = new ArrayList<>();

    SRTranslateVH translateVH;

    ArrayList<SRTract> mTracts;
    String mBookId;
    String mCatalogueId;
    String mGroupId;
    String mTaskId;
    String mTitle;

    int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_dubbing);

        if (SRUserManager.getInstance().isGuesterUser(true)) {
            finish();
            return;
        }

        mTracts = (ArrayList<SRTract>) getIntent().getSerializableExtra(TRACTS);
        mBookId = getIntent().getStringExtra(BOOK_ID);
        mCatalogueId = getIntent().getStringExtra(CATALOGUE_ID);
        mGroupId = getIntent().getStringExtra(GROUP_ID);
        mTaskId = getIntent().getStringExtra(TASK_DI);
        mTitle = getIntent().getStringExtra(TITLE);

        mActionBar.showTitle(mTitle);
        SRDubbingFragment fragment;
        int pageIndex = 0;
        for (SRTract tract : mTracts) {
            fragment = new SRDubbingFragment();
            fragment.setTranslateListener(this);
            new SRDubbingPresenter(fragment, tract, mCatalogueId, mTaskId, mGroupId, pageIndex, mBookId);
            mFragments.add(fragment);
            pageIndex++;
        }

        mAdapter = new DubbingAdapter(getSupportFragmentManager());
        mViewPage.setAdapter(mAdapter);
//        mViewPage.setPageTransformer(false, new ScaleTransformer());
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTextPage.setText((position + 1) + "/" + mFragments.size());
                if (position >= mFragments.size() - 1) {
                    layoutSubmit.setVisibility(View.VISIBLE);
                    mTextPage.setVisibility(View.GONE);
                } else {
                    mTextPage.setVisibility(View.VISIBLE);
                    layoutSubmit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.layoutSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutSubmit:
                uploadMergedTractAudio();
                break;
        }
    }

    @Override
    public void onTranslate(String word) {
        ZYLog.e(getClass().getSimpleName(), "translate: " + word);
        if (translateVH == null) {
            translateVH = new SRTranslateVH();
            translateVH.attachTo(mRootView);
        }
        translateVH.show(null);
        TranslateRequest.getRequest().translate(word, new TranslateRequest.TranslateRequestCallBack() {
            @Override
            public void translateCallBack(YouDaoBean translateBean, String errorMsg) {
                if (translateBean != null) {
                    translateVH.show(translateBean);
                } else {
                    ZYToast.show(SRDubbingActivity.this, errorMsg == null ? "网络错误,请重试尝试!" : errorMsg);
                    translateVH.hide();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (translateVH != null && translateVH.isVisibility()) {
            translateVH.hide();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    private class DubbingAdapter extends FragmentStatePagerAdapter {

        public DubbingAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public View getViewAt(int position) {
            return mFragments.get(position).getRootView();
        }
    }

    private class ScaleTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

        private float mLastOffset;

        public ScaleTransformer() {
            mViewPage.addOnPageChangeListener(this);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realCurrentPosition;
            int nextPosition;
            float realOffset;
            boolean goingLeft = mLastOffset > positionOffset;

            if (goingLeft) {
                realCurrentPosition = position + 1;
                nextPosition = position;
                realOffset = 1 - positionOffset;
            } else {
                nextPosition = position + 1;
                realCurrentPosition = position;
                realOffset = positionOffset;
            }

            if (realCurrentPosition > mAdapter.getCount() - 1) {
                realCurrentPosition = mAdapter.getCount() - 1;
            }
            View currentView = mAdapter.getViewAt(realCurrentPosition);
            if (currentView != null) {
                currentView.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                currentView.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }

            if (nextPosition > mAdapter.getCount() - 1) {
                return;
            }

            View nextView = mAdapter.getViewAt(nextPosition);
            if (nextView != null) {
                nextView.setScaleX((float) (1 + 0.1 * realOffset));
                nextView.setScaleY((float) (1 + 0.1 * realOffset));
            }

            mLastOffset = positionOffset;
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void transformPage(View page, float position) {

        }
    }

    public void uploadMergedTractAudio() {
        if (mTracts != null && mTracts.size() > 0) {
            mergeTractAudioPath = SRApplication.MERGE_AUDIO_ROOT_DIR + mBookId + "_" + mCatalogueId + ".wav";
            try {
                SRMarkBean markBean;
                float startTime = 0;
                float endTime = 0;
                int totalScore = 0;
                final JSONObject tractObject = new JSONObject();
                final ArrayList<File> audioFiles = new ArrayList<File>();
                File audio;
                for (SRTract tract : mTracts) {
                    markBean = tract.getMarkBean();
                    if (markBean.getScore() > 0 && markBean.getAudioPath() != null) {
                        audio = new File(markBean.getAudioPath());
                        if (audio.exists()) {
                            totalScore += markBean.getScore();
                            audioFiles.add(audio);
                            endTime = startTime + (tract.getTrack_auend() - tract.getTrack_austart());
                            tractObject.put(tract.getTrack_id() + "", startTime + "," + endTime);
                            startTime = endTime + 0.3f;
                        }
                    }
                }

                if (audioFiles.size() > 0) {
                    showProgress();
                    score = totalScore / audioFiles.size();
                    final File outFile = new File(mergeTractAudioPath);
                    outFile.delete();
                    outFile.createNewFile();
                    Observable observable = Observable.create(new Observable.OnSubscribe<File>() {
                        @Override
                        public void call(Subscriber<? super File> subscriber) {
                            try {
                                ZYWavMergeUtils.mergeWav(audioFiles, outFile);
                                subscriber.onNext(outFile);
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                            subscriber.onCompleted();
                        }
                    });
                    mSubscriptions.add(ZYNetSubscription.subscription(observable, new Subscriber<File>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ZYLog.e(SRMarkPresenter.class.getSimpleName(), "error: " + e.getMessage());
                        }

                        @Override
                        public void onNext(File outFile) {
                            ZYLog.e(SRMarkPresenter.class.getSimpleName(), "merge-success: " + tractObject.toString());
                            uploadMergeAudioToQiNiu(outFile.getAbsolutePath(), tractObject.toString());
                        }
                    }));
                } else {
                    //没有合成的配音
                    showToast("还没有录音哦!");
                }
            } catch (Exception e) {
                ZYLog.e(getClass().getSimpleName(), "uploadMergedTractAudio-error: " + e.getMessage());
                hideProgress();
            }
        }
    }

    private void uploadMergeAudioToQiNiu(final String audioPath, final String tractValues) {
        String key = getTime() + File.separator + System.currentTimeMillis()
                + SRUserManager.getInstance().getUser().uid + ".wav";

        ZYUtils.uploadFile(SRApplication.getInstance().getCurrentActivity(), key, audioPath, SRUserManager.getInstance().getUser().upload_token, new CallBack() {
            @Override
            public void onProcess(long current, long total) {

            }

            @Override
            public void onSuccess(UploadCallRet ret) {
                if (ret != null) {
                    try {
                        String picKey = ret.getKey();
                        ZYLog.e(SRMarkFragment.class.getSimpleName(), "uploadMergeAudio-key: " + picKey);
                        uploadmergeAudioToServers(picKey, tractValues);
                    } catch (Exception e) {
                        hideProgress();
                        ZYToast.show(SRApplication.getInstance().getCurrentActivity(), e.getMessage() + "");
                    }
                } else {
                    hideProgress();
                    ZYToast.show(SRApplication.getInstance().getCurrentActivity(), "上传失败,请重试");
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
                    ZYToast.show(SRApplication.getInstance().getCurrentActivity(), "上传失败: " + ret.getStatusCode());
                }
            }
        });
    }

    private void uploadmergeAudioToServers(final String qiniuKey, final String tractValues) {
        mSubscriptions.add(ZYNetSubscription.subscription(new SRMarkModel().catalogueAdd(qiniuKey, mBookId, mCatalogueId, score, tractValues, mGroupId, mTaskId), new ZYNetSubscriber<ZYResponse<SRCatalogueResponse>>() {
            @Override
            public void onSuccess(ZYResponse<SRCatalogueResponse> response) {
                super.onSuccess(response);
                hideProgress();
                if (response.data != null) {
                    ZYLog.e(SRMarkPresenter.class.getSimpleName(), "uploadMergeAudio-succ:" + response.data.share_url);
                    uploadMergeAudioSuc(response.data);
                } else {
                    onFail("网络异常,请重新尝试!");
                }
            }

            @Override
            public void onFail(String message) {
                hideProgress();
                super.onFail(message);
            }
        }));
    }

    public void uploadMergeAudioSuc(SRCatalogueResponse response) {
        if (markShareVH == null) {
            markShareVH = new SRMarkShareVH();
            markShareVH.bindView(LayoutInflater.from(this).inflate(markShareVH.getLayoutResId(), mRootView, false));
            mRootView.addView(markShareVH.getItemView());
        }
        markShareVH.updateView(response, 0);
        if (!TextUtils.isEmpty(mGroupId)) {
            markShareVH.setTitle("任务配音上传成功!");
        }
    }

    private String getTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}