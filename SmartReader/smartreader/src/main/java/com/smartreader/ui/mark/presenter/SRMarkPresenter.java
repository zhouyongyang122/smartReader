package com.smartreader.ui.mark.presenter;

import com.smartreader.base.mvp.ZYBasePresenter;
import com.smartreader.ui.main.contract.SRMainContract;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.ui.mark.contract.SRMarkContract;
import com.smartreader.ui.mark.model.SRMarkModel;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkPresenter extends ZYBasePresenter implements SRMarkContract.IPresenter {

    SRMarkContract.IView iView;

    SRMarkModel model;

    SRPage page;

    String bookId;

    private RandomAccessFile mRandomAccessFileCompose;
    private RandomAccessFile mRandomAccessFileBg;
    private FileOutputStream mCurPcmFileOutputStream;
    private long mTotalDataLength;
    private byte[] readDataBuffer;
    private ByteBuffer byteBuf;
    private long beginOffset;
    private long nullDataLength;
    private String mDecodeMp3FilePath;

    public SRMarkPresenter(SRMarkContract.IView iView, SRPage page, String bookId) {
        this.iView = iView;
        model = new SRMarkModel();
        this.iView.setPresenter(this);
        this.page = page;
        this.bookId = bookId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
