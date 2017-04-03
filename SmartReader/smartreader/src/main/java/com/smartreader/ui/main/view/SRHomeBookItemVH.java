package com.smartreader.ui.main.view;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.SRApplication;
import com.smartreader.base.view.ZYCircleProgressView;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.service.db.ZYDBManager;
import com.smartreader.service.downNet.down.ZYDownState;
import com.smartreader.service.downNet.down.ZYDownloadManager;
import com.smartreader.service.downNet.down.ZYDownloadScriberListener;
import com.smartreader.service.downNet.down.ZYDownloadService;
import com.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.smartreader.ui.main.model.SRBookFileManager;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.utils.ZYLog;
import com.smartreader.utils.ZYScreenUtils;
import com.smartreader.utils.ZYToast;

import java.io.IOException;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/28.
 */

public class SRHomeBookItemVH extends ZYBaseViewHolder<SRBook> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.progressView)
    ZYCircleProgressView progressView;

    @Bind(R.id.textStatus)
    TextView textStatus;

    @Bind(R.id.imgAdd)
    ImageView imgAdd;

    @Bind(R.id.cardView)
    CardView cardView;

    SRBook mData;

    private HomeBookItemListener listener;

    public SRHomeBookItemVH(HomeBookItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
        float scale = 105.0f / 155.0f;
        float width = ZYScreenUtils.getScreenWidth(SRApplication.getInstance()) - ZYScreenUtils.dp2px(SRApplication.getInstance(), 15 + 15);
        width = width - ZYScreenUtils.dp2px(SRApplication.getInstance(), 14 + 14);
        width = width / 3.0f;
        float height = width / scale;

        layoutParams.height = (int) height;

        imgBg.setLayoutParams(layoutParams);
    }

    @Override
    public void updateView(SRBook data, final int position) {
        if (data != null) {
            ZYLog.e(getClass().getSimpleName(), "updateView: " + position);
            mData = data;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onHomeBookItemClick(mData, position);
                }
            });
            if (data.getBook_id_int() < 0) {
                //添加按钮
                data.setListener(null);
                imgBg.setImageResource(R.color.c5);
                imgAdd.setVisibility(View.VISIBLE);
                textStatus.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
            } else {
                imgAdd.setVisibility(View.GONE);
                ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, data.getPic(), R.color.c5, R.color.c5);
                if (data.isFinished()) {
                    data.setListener(null);
                    textStatus.setVisibility(View.GONE);
                    progressView.setVisibility(View.GONE);
                } else {
                    data.setListener(downloadScriberListener);
                    textStatus.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.VISIBLE);
                    float progress = (float) data.getCurrent() * 100.0f / (float) data.getTotal();
                    progressView.setProgress((int) progress);
                    textStatus.setText(mData.getStateString());
                    if (data.getState().getState() != ZYDownState.PAUSE.getState()) {
                        ZYDownloadManager.getInstance().startDown(data);
                    }

                    textStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (mData.getState().getState() == ZYDownState.UNZIP.getState()) {
                                ZYToast.show(mContext, "解压中,请稍等!");
                                return;
                            }

                            if (mData.getState().getState() == ZYDownState.UNZIPERROR.getState()) {
                                mData.setState(ZYDownState.UNZIP);
                                textStatus.setText(mData.getStateString());
                                String unZipPath = SRBookFileManager.getBookPath(mData.book_id);
                                try {
                                    SRBookFileManager.unZip(mData.getSavePath(), unZipPath);
                                    mData.setSavePath(unZipPath);
                                    mData.setState(ZYDownState.FINISH);
                                    textStatus.setText(mData.getStateString());
                                    textStatus.setVisibility(View.GONE);
                                    progressView.setVisibility(View.GONE);
                                    mData.update();
                                } catch (IOException e) {
                                    mData.setState(ZYDownState.UNZIPERROR);
                                    textStatus.setText(mData.getStateString());
                                    ZYToast.show(mContext, "如果多次解压失败,请删除后重新下载!");
                                }
                                return;
                            }

                            if (mData.getState().getState() != ZYDownState.PAUSE.getState()) {
                                ZYDownloadManager.getInstance().pauseDown(mData);
                                textStatus.setText(mData.getStateString());
                            } else {
                                mData.setState(ZYDownState.WAIT);
                                textStatus.setText(mData.getStateString());
                                ZYDownloadManager.getInstance().startDown(mData);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_home_book_item;
    }

    ZYDownloadScriberListener downloadScriberListener = new ZYDownloadScriberListener() {
        @Override
        public void onStart() {
            textStatus.setText(mData.getStateString());
        }

        @Override
        public void onComplete(Object t) {
            ZYLog.e(getClass().getSimpleName(), "onComplete");
            textStatus.setVisibility(View.GONE);
            textStatus.setText(mData.getStateString());
            progressView.setVisibility(View.GONE);
        }

        @Override
        public void updateProgress(long current, long total) {
//            ZYLog.e(getClass().getSimpleName(), "updateProgress:" + current);
            float progress = (float) current * 100.0f / (float) total;
            textStatus.setText("下载中" + (int) progress + "%");
            progressView.setProgress((int) progress);
        }

        @Override
        public void onUnZip() {
            ZYLog.e(getClass().getSimpleName(), "onUnZip。。。。");
            textStatus.setText(mData.getStateString());
        }

        @Override
        public void onError(String message) {
            ZYLog.e(getClass().getSimpleName(), "onError:" + message);
            textStatus.setText(mData.getStateString());
        }

        @Override
        public void onPuase() {
            textStatus.setText(mData.getStateString());
        }

        @Override
        public void onStop() {
            textStatus.setText(mData.getStateString());
        }
    };

    public interface HomeBookItemListener {
        void onHomeBookItemClick(SRBook book, int position);
    }
}