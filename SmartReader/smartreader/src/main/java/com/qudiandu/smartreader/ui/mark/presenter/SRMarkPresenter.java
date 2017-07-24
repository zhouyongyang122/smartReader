package com.qudiandu.smartreader.ui.mark.presenter;

import com.qiniu.rs.CallBack;
import com.qiniu.rs.CallRet;
import com.qiniu.rs.UploadCallRet;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.contract.SRMarkContract;
import com.qudiandu.smartreader.ui.mark.model.SRMarkModel;
import com.qudiandu.smartreader.ui.mark.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkResponse;
import com.qudiandu.smartreader.ui.mark.view.SRMarkFragment;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;
import com.qudiandu.smartreader.utils.ZYUtils;
import com.qudiandu.smartreader.utils.ZYWavMergeUtils;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkPresenter extends ZYBasePresenter implements SRMarkContract.IPresenter {

    SRMarkContract.IView iView;

    SRMarkModel model;

    String bookId;

    ArrayList<SRTract> mTracts;

    SRMarkBean markBean;

    String catalogue_id;

    String mergeTractAudioPath;

    String group_id;

    String task_id;

    int score;

    public SRMarkPresenter(SRMarkContract.IView iView, ArrayList<SRTract> tracts, String bookId, String catalogue_id, String group_id, String task_id) {
        this.iView = iView;
        model = new SRMarkModel();
        this.iView.setPresenter(this);
        mTracts = tracts;
        this.bookId = bookId;
        this.task_id = task_id;
        this.catalogue_id = catalogue_id;
        this.group_id = group_id;

        for (SRTract tract : tracts) {
            tract.isRecordType = true;
            break;
        }
    }

    public void uploadTractAudio(SRTract tract) {
        iView.showProgress();
        markBean = tract.getMarkBean();
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("book_id", bookId.equals("0") ? "1" : bookId);
        paramas.put("page_id", tract.getPage_id() + "");
        paramas.put("track_id", tract.getTrack_id() + "");
        paramas.put("score", markBean.score + "");
        paramas.put("audio", tract.audioQiNiuKey);

        mSubscriptions.add(ZYNetSubscription.subscription(model.trackAdd(paramas), new ZYNetSubscriber<ZYResponse<SRMarkResponse>>() {
            @Override
            public void onSuccess(ZYResponse<SRMarkResponse> response) {
                super.onSuccess(response);
                iView.hideProgress();
                if (response.data != null) {
                    markBean.show_track_id = response.data.show_track_id;
                    markBean.share_url = response.data.share_url;
                    markBean.update();
                    iView.uploadAudioSuc(markBean);
                } else {
                    onFail("网络异常,请重新尝试!");
                }
            }

            @Override
            public void onFail(String message) {
                iView.hideProgress();
                super.onFail(message);
            }
        }));
    }

    public void uploadMergedTractAudio() {
        if (mTracts != null && mTracts.size() > 0) {
            mergeTractAudioPath = SRApplication.MERGE_AUDIO_ROOT_DIR + bookId + "_" + catalogue_id + ".wav";
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
                    iView.showProgress();
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
                    iView.showToast("还没有录音哦!");
                }
            } catch (Exception e) {
                ZYLog.e(getClass().getSimpleName(), "uploadMergedTractAudio-error: " + e.getMessage());
                iView.hideProgress();
            }
        }
    }

    private void uploadmergeAudioToServers(final String qiniuKey, final String tractValues) {
        mSubscriptions.add(ZYNetSubscription.subscription(model.catalogueAdd(qiniuKey, bookId, catalogue_id, score, tractValues), new ZYNetSubscriber<ZYResponse<SRCatalogueResponse>>() {
            @Override
            public void onSuccess(ZYResponse<SRCatalogueResponse> response) {
                super.onSuccess(response);
                iView.hideProgress();
                if (response.data != null) {
                    ZYLog.e(SRMarkPresenter.class.getSimpleName(), "uploadMergeAudio-succ:" + response.data.share_url);
                    iView.uploadMergeAudioSuc(response.data);
                } else {
                    onFail("网络异常,请重新尝试!");
                }
            }

            @Override
            public void onFail(String message) {
                iView.hideProgress();
                super.onFail(message);
            }
        }));
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
                        iView.hideProgress();
                        ZYToast.show(SRApplication.getInstance().getCurrentActivity(), e.getMessage() + "");
                    }
                } else {
                    iView.hideProgress();
                    ZYToast.show(SRApplication.getInstance().getCurrentActivity(), "上传失败,请重试");
                }
            }

            @Override
            public void onFailure(CallRet ret) {
                iView.hideProgress();
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

    private String getTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public ArrayList<SRTract> getTracks() {
        return mTracts;
    }
}
