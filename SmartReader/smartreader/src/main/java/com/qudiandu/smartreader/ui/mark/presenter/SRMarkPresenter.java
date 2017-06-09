package com.qudiandu.smartreader.ui.mark.presenter;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.contract.SRMarkContract;
import com.qudiandu.smartreader.ui.mark.model.SRMarkModel;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkPresenter extends ZYBasePresenter implements SRMarkContract.IPresenter {

    SRMarkContract.IView iView;

    SRMarkModel model;

    String bookId;

    ArrayList<SRTract> mTracts;

    SRMarkBean markBean;

    public SRMarkPresenter(SRMarkContract.IView iView, ArrayList<SRTract> tracts, String bookId) {
        this.iView = iView;
        model = new SRMarkModel();
        this.iView.setPresenter(this);
        mTracts = tracts;
        this.bookId = bookId;

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

    public ArrayList<SRTract> getTracks() {
        return mTracts;
    }
}
