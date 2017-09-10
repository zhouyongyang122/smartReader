package com.qudiandu.smartreader.ui.book.presenter;

import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.book.contract.SRBookUnitsContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRBookJson;
import com.qudiandu.smartreader.ui.main.model.bean.SRCatalogue;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.main.presenter.SRBookHomePresenter;
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
 * Created by ZY on 17/9/7.
 */

public class SRBookUnitsPresenter extends ZYBasePresenter implements SRBookUnitsContract.IPresenter {

    String mLocalPath;

    SRBookUnitsContract.IView mView;

    SRBook mBook;

    public SRBookUnitsPresenter(SRBookUnitsContract.IView view, String localPath) {
        mView = view;
        mView.setPresenter(this);
        mLocalPath = localPath;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        mView.showProgress();
        Observable observable = Observable.create(new Observable.OnSubscribe<SRBook>() {
            @Override
            public void call(Subscriber<? super SRBook> subscriber) {
                try {
                    InputStream inputStream = null;
                    if (mLocalPath.startsWith("file:///")) {
                        inputStream = SRApplication.getInstance().getAssets().open("1/book.json");
                    } else {
                        inputStream = new FileInputStream(new File(mLocalPath + "book.json"));
                    }
                    JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                    Gson gson = new Gson();
                    SRBookJson bookJson = gson.fromJson(reader, SRBookJson.class);
                    SRBook book = bookJson.book;

                    //设置tract的音频路径和track对应的mark的id....
                    for (SRPage page : book.getPage()) {
                        for (SRTract tract : page.getTrack()) {
                            tract.setMp3Path(mLocalPath + "mp3/" + tract.getMp3name());
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
                        page.setLocalRootDirPath(mLocalPath);
                    }

                    for (SRCatalogue catalogue : book.catalogue) {
                        for (SRPage page : book.page) {
                            if (TextUtils.isEmpty(catalogue.getPage_url()) && catalogue.containsPage(page.getPage_id() + "")) {
                                catalogue.setPage_url(page.getPicPath());
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
                mView.hideProgress();
                ZYLog.e(SRBookHomePresenter.class.getSimpleName(), "error: " + e.getMessage());
            }

            @Override
            public void onNext(SRBook book) {
                mView.hideProgress();
                mBook = book;
                mView.showList(book.catalogue);
            }
        }));
    }

    public void toDubbing(int cateId) {
        ArrayList<SRTract> tracts = new ArrayList<SRTract>();
        for (SRPage page : mBook.getPage()) {
            if (page.getCatalogueId() == cateId) {
                tracts.addAll(page.getTrack());
            } else if (tracts.size() > 0) {
                break;
            }
        }
        mView.toDubbing(tracts, mBook.book_id, cateId + "");
    }
}
