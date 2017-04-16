package com.smartreader.ui.mark.presenter;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.service.net.ZYNetSubscriber;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.main.contract.SRMainContract;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.ui.mark.contract.SRMarkContract;
import com.smartreader.ui.mark.model.SRMarkModel;
import com.smartreader.ui.mark.model.bean.SRMarkBean;
import com.smartreader.ui.mark.model.bean.SRMarkResponse;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkPresenter extends ZYBasePresenter implements SRMarkContract.IPresenter {

    SRMarkContract.IView iView;

    SRMarkModel model;

    SRPage page;

    String bookId;

    SRMarkBean markBean;

    public SRMarkPresenter(SRMarkContract.IView iView, SRPage page, String bookId) {
        this.iView = iView;
        model = new SRMarkModel();
        this.iView.setPresenter(this);
        this.page = page;
        this.bookId = bookId;
    }

    public void uploadTractAudio(SRTract tract) {
        iView.showProgress();
        markBean = tract.getMarkBean(getMarkId(tract.getTrack_id() + ""));
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("book_id", bookId.equals("0") ? "1" : bookId);
        paramas.put("page_id", page.getPage_id() + "");
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

    public SRPage getPage() {
        return page;
    }

    public String getBookId() {
        return bookId;
    }

    public String getMarkId(String tractId) {
        return bookId + "_" + page.getPage_id() + "_" + tractId;
    }

    public List<SRTract> getTracks() {
        return page.getTrack();
    }
}
