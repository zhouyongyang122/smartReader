package com.qudiandu.smartreader.ui.dubbing.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.xiansheng.XSBean;
import com.qudiandu.smartreader.ui.dubbing.contract.SRDubbingContract;
import com.qudiandu.smartreader.ui.dubbing.model.SRDubbingModel;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkBean;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkResponse;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZY on 17/12/1.
 */

public class SRDubbingPresenter extends ZYBasePresenter implements SRDubbingContract.IPresenter {

    int mPageId;

    String mGroupId;

    SRTract mTrack;

    String mCatalogueId;

    String mTaskId;

    String mBookId;

    SRDubbingModel mModel;

    SRDubbingContract.IView mView;

    public SRDubbingPresenter(SRDubbingContract.IView iView, SRTract tract, String catalogueId, String taskId, String groupId, int pageId, String bookId) {
        mModel = new SRDubbingModel();
        mGroupId = groupId;
        mTrack = tract;
        mCatalogueId = catalogueId;
        mTaskId = taskId;
        mBookId = bookId;
        mPageId = pageId;
        mView = iView;
        mView.setPresenter(this);
    }

    @Override
    public String getGroupId() {
        return mGroupId;
    }

    public SRTract getTrack() {
        return mTrack;
    }

    public String getCatalogueId() {
        return mCatalogueId;
    }

    public String getTaskId() {
        return mTaskId;
    }

    public int getPageId() {
        return mPageId;
    }

    @Override
    public void uploadTractAudio() {
        mView.showProgress();
        final SRMarkBean markBean = mTrack.getMarkBean();
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("book_id", mBookId.equals("0") ? "1" : mBookId);
        paramas.put("page_id", mTrack.getPage_id() + "");
        paramas.put("track_id", mTrack.getTrack_id() + "");
        paramas.put("score", markBean.score + "");
        paramas.put("audio", mTrack.audioQiNiuKey);
        XSBean xsBean = markBean.getScoreBean();
        try {
            JSONObject jsonObject = new JSONObject();
            if (xsBean != null && xsBean.result != null) {
                jsonObject.put("0", xsBean.result.getFluency() + "");
                jsonObject.put("1", xsBean.result.getIntegrity() + "");
                jsonObject.put("2", xsBean.result.getAccuracy() + "");
            } else {
                jsonObject.put("0", "0");
                jsonObject.put("1", "0");
                jsonObject.put("2", "0");
            }
            paramas.put("other_score", jsonObject.toString());
        } catch (Exception e) {

        }

        mSubscriptions.add(ZYNetSubscription.subscription(mModel.trackAdd(paramas), new ZYNetSubscriber<ZYResponse<SRMarkResponse>>() {
            @Override
            public void onSuccess(ZYResponse<SRMarkResponse> response) {
                super.onSuccess(response);
                mView.hideProgress();
                if (response.data != null) {
                    markBean.show_track_id = response.data.show_track_id;
                    markBean.share_url = response.data.share_url;
                    markBean.update();
                    mView.uploadAudioSuc(markBean);
                } else {
                    onFail("网络异常,请重新尝试!");
                }
            }

            @Override
            public void onFail(String message) {
                mView.hideProgress();
                super.onFail(message);
            }
        }));
    }
}
