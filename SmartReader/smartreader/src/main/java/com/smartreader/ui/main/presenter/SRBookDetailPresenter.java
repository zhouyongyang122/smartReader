package com.smartreader.ui.main.presenter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.smartreader.SRApplication;
import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.service.net.ZYNetSubscription;
import com.smartreader.ui.main.contract.SRBookDetailContract;
import com.smartreader.ui.main.model.SRBookDetailModel;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.main.model.bean.SRBookJson;
import com.smartreader.ui.main.model.bean.SRCatalogue;
import com.smartreader.utils.ZYLog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                    subscriber.onNext(bookJson.book);
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
}
