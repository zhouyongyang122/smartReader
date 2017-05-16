package com.qudiandu.smartreader.ui.main.presenter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.contract.SRBookDetailContract;
import com.qudiandu.smartreader.ui.main.model.SRBookDetailModel;
import com.qudiandu.smartreader.ui.main.model.SRPageManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRBookJson;
import com.qudiandu.smartreader.ui.main.model.bean.SRCatalogue;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.utils.ZYLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ZY on 17/3/29.
 */

public class SRBookDetailPresenter extends ZYBasePresenter implements SRBookDetailContract.IPresenter {

    private SRBookDetailContract.IView iView;

    private SRBookDetailModel model;

    private String localRootDirPath;

    private SRBook bookData;

//    private int curPageId = 1;

    private boolean needShowSentenceBg = true;

    private boolean isSingleRepeat = false;

    private boolean isRepeats = false;

    private SRTract repeatsStartTract;

    public SRBookDetailPresenter(SRBookDetailContract.IView iView, String localRootDirPath) {
        this.iView = iView;
        model = new SRBookDetailModel();
        iView.setPresenter(this);
        this.localRootDirPath = localRootDirPath;
    }

    @Override
    public void subscribe() {
        super.subscribe();

        Observable observable = Observable.create(new Observable.OnSubscribe<SRBook>() {
            @Override
            public void call(Subscriber<? super SRBook> subscriber) {
                try {
                    InputStream inputStream = null;
                    if (localRootDirPath.startsWith("file:///")) {
                        inputStream = SRApplication.getInstance().getAssets().open("1/book.json");
                    } else {
                        inputStream = new FileInputStream(new File(localRootDirPath + "/book.json"));
                    }
                    JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                    Gson gson = new Gson();
                    SRBookJson bookJson = gson.fromJson(reader, SRBookJson.class);

                    SRBook book = bookJson.book;

                    SRBook dbBook = SRBook.queryById(book.book_id);
                    if (dbBook != null) {
                        book.lastPageIndex = dbBook.lastPageIndex;
                    }

                    //设置tract的音频路径
                    for (SRPage page : book.getPage()) {
                        for (SRTract tract : page.getTrack()) {
                            tract.setMp3Path(localRootDirPath + "mp3/" + tract.getMp3name());
                        }
                    }
                    subscriber.onNext(book);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
        mSubscriptions.add(ZYNetSubscription.subscription(observable, new Subscriber<SRBook>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ZYLog.e(SRBookDetailPresenter.class.getSimpleName(), "error: " + e.getMessage());
            }

            @Override
            public void onNext(SRBook book) {
                bookData = book;

                List<SRCatalogue> catalogues = new ArrayList<SRCatalogue>();
                String lastUnit = "";
                for (SRCatalogue catalogue : bookData.catalogue) {
                    if (!catalogue.getUnit().equals(lastUnit)) {
                        lastUnit = catalogue.getUnit();
                        catalogues.add(getUnit(lastUnit));
                    }
                    catalogues.add(catalogue);
                }
                bookData.setCatalogue(catalogues);

                iView.showBookData(bookData);
            }
        }));
    }

    @Override
    public void onSelecteTrack(SRTract selTract) {
        if (isRepeats) {
            if (repeatsStartTract == null) {
                repeatsStartTract = selTract;
                iView.selRepeatsEnd();
            } else {
                if (repeatsStartTract.getTrack_id() >= selTract.getTrack_id()) {
                    iView.showToast("复读终点只能选择起点后面的位置!");
                    return;
                }

                ArrayList<SRTract> repeatTracts = new ArrayList<SRTract>();
                for (SRPage page : bookData.getPage()) {
                    for (SRTract tract : page.getTrack()) {
                        if (tract.getTrack_id() >= repeatsStartTract.getTrack_id() && tract.getTrack_id() <= selTract.getTrack_id()) {
                            repeatTracts.add(tract);
                            if (tract.getTrack_id() == selTract.getTrack_id()) {
                                break;
                            }
                        }
                    }
                }
                SRPageManager.getInstance().startRepeats(repeatTracts);
                iView.playRepeats();
            }
            return;
        }
        if (isSingleRepeat) {
            ArrayList<SRTract> repeatTracts = new ArrayList<SRTract>();
            repeatTracts.add(selTract);
            SRPageManager.getInstance().startRepeats(repeatTracts);
        } else {
            SRPageManager.getInstance().startAudio(selTract.getMp3Path(), selTract.getAudioStart(), selTract.getAudioEnd());
        }
    }

    @Override
    public void stopSingleRepeat() {
        isSingleRepeat = false;
        SRPageManager.getInstance().stopRepeats();
    }

    @Override
    public void stopRepeats() {
        isRepeats = false;
        repeatsStartTract = null;
        SRPageManager.getInstance().stopRepeats();
    }

    @Override
    public void puaseRepeats() {
        SRPageManager.getInstance().pauseAudio();
    }

    public void continueRepeats() {
        SRPageManager.getInstance().continueStartRepeats();
    }

    private SRCatalogue getUnit(String unit) {
        SRCatalogue catalogueUnit = new SRCatalogue();
        catalogueUnit.setUnit(unit);
        catalogueUnit.setCatalogue_id(-1);
        return catalogueUnit;
    }

    public SRBook getBookData() {
        return bookData;
    }

    public String getLocalRootDirPath() {
        return localRootDirPath;
    }

//    public int getCurPageId() {
//        return curPageId;
//    }
//
//    public void setCurPageId(int curPageId) {
//        this.curPageId = curPageId;
//    }

    public boolean isRepeats() {
        return isRepeats;
    }

    public void setRepeats(boolean repeats) {
        isRepeats = repeats;
    }

    public boolean isSingleRepeat() {
        return isSingleRepeat;
    }

    public void setSingleRepeat(boolean singleRepeat) {
        isSingleRepeat = singleRepeat;
    }

    public boolean isNeedShowSentenceBg() {
        return needShowSentenceBg;
    }

    public void setNeedShowSentenceBg(boolean needShowSentenceBg) {
        this.needShowSentenceBg = needShowSentenceBg;
    }
}
