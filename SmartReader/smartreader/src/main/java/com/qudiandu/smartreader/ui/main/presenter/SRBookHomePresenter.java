package com.qudiandu.smartreader.ui.main.presenter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.main.contract.SRBookHomeContract;
import com.qudiandu.smartreader.ui.main.model.SRBookHomeModel;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
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

public class SRBookHomePresenter extends ZYBasePresenter implements SRBookHomeContract.IPresenter {

    private SRBookHomeContract.IView iView;

    private SRBookHomeModel model;

    private String localRootDirPath;

    private SRBook bookData;

    private boolean needShowSentenceBg = true;

    private boolean isSingleRepeat = false;

    private boolean isRepeats = false;

    private SRTract repeatsStartTract;

    public SRBookHomePresenter(SRBookHomeContract.IView iView, String localRootDirPath) {
        this.iView = iView;
        model = new SRBookHomeModel();
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

                    //设置最后浏览的页数
                    if (dbBook != null) {
                        book.lastPageIndex = dbBook.lastPageIndex;
                    }

                    //整理单元列表:
                    List<SRCatalogue> catalogues = new ArrayList<SRCatalogue>();
                    String lastUnit = "";
                    for (SRCatalogue catalogue : book.catalogue) {
                        if (!catalogue.getUnit().equals(lastUnit)) {
                            lastUnit = catalogue.getUnit();
                            catalogues.add(getUnit(lastUnit));
                        }
                        catalogues.add(catalogue);
                    }
                    book.setCatalogue(catalogues);

                    //设置tract的音频路径和track对应的mark的id....
                    for (SRPage page : book.getPage()) {
                        for (SRTract tract : page.getTrack()) {
                            tract.setMp3Path(localRootDirPath + "mp3/" + tract.getMp3name());
                            tract.setBook_id(book.getBook_id_int());
                            tract.setMarkId(book.getId() + "_" + page.getPage_id() + "_" + tract.getTrack_id());
                        }

                        //设置page对应的单元小节id
                        for (SRCatalogue catalogue : book.catalogue) {
                            if (catalogue.containsPage(page.getPage_id() + "")) {
                                page.setCatalogueId(catalogue.getCatalogue_id());
                                break;
                            }
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
                ZYLog.e(SRBookHomePresenter.class.getSimpleName(), "error: " + e.getMessage());
            }

            @Override
            public void onNext(SRBook book) {
                bookData = book;
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
                SRPlayManager.getInstance().startRepeats(repeatTracts);
                iView.playRepeats();
            }
            return;
        }
        if (isSingleRepeat) {
            ArrayList<SRTract> repeatTracts = new ArrayList<SRTract>();
            repeatTracts.add(selTract);
            SRPlayManager.getInstance().startRepeats(repeatTracts);
        } else {
            SRPlayManager.getInstance().startAudio(selTract.getMp3Path(), selTract.getAudioStart(), selTract.getAudioEnd());
        }
    }

    @Override
    public void stopSingleRepeat() {
        isSingleRepeat = false;
        SRPlayManager.getInstance().stopRepeats();
    }

    @Override
    public void stopRepeats() {
        isRepeats = false;
        repeatsStartTract = null;
        SRPlayManager.getInstance().stopRepeats();
    }

    @Override
    public void puaseRepeats() {
        SRPlayManager.getInstance().pauseAudio();
    }

    public void continueRepeats() {
        SRPlayManager.getInstance().continueStartRepeats();
    }

    private SRCatalogue getUnit(String unit) {
        SRCatalogue catalogueUnit = new SRCatalogue();
        catalogueUnit.setUnit(unit);
        catalogueUnit.setCatalogue_id(-1);
        return catalogueUnit;
    }

    public ArrayList<SRTract> getTractsByCatalogueId(int catalogueId) {
        ArrayList<SRTract> tracts = new ArrayList<SRTract>();
        for (SRPage page : bookData.getPage()) {
            if (page.getCatalogueId() == catalogueId) {
                tracts.addAll(page.getTrack());
            } else if (tracts.size() > 0) {
                break;
            }
        }
        return tracts;
    }

    public SRBook getBookData() {
        return bookData;
    }

    public String getLocalRootDirPath() {
        return localRootDirPath;
    }

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
