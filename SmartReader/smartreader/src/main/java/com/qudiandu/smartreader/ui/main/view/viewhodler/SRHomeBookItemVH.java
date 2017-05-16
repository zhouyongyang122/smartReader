package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.view.ZYCircleProgressView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.service.downNet.down.ZYDownState;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadManager;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadScriberListener;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.SRBookFileManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYScreenUtils;
import com.qudiandu.smartreader.utils.ZYToast;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/28.
 */

public class SRHomeBookItemVH extends ZYBaseViewHolder<SRBook> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.progressView)
    ZYCircleProgressView progressView;

    @Bind(R.id.progressBgView)
    View progressBgView;

    @Bind(R.id.textStatus)
    TextView textStatus;

    @Bind(R.id.cardView)
    CardView cardView;

    @Bind(R.id.imgDel)
    ImageView imgDel;

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
        width = width - ZYScreenUtils.dp2px(SRApplication.getInstance(), 15 + 15);
        width = width / 3.0f;
        float height = width / scale;

        layoutParams.height = (int) height;

        imgBg.setLayoutParams(layoutParams);

        layoutParams = (RelativeLayout.LayoutParams) progressBgView.getLayoutParams();
        layoutParams.height = (int) height;
        progressBgView.setLayoutParams(layoutParams);
    }

    @Override
    public void updateView(SRBook data, final int position) {
        if (data != null) {
            ZYLog.e(getClass().getSimpleName(), "updateView: " + position);
            mData = data;

            if (mData.isDeleteStatus) {
                imgDel.setVisibility(View.VISIBLE);
            } else {
                imgDel.setVisibility(View.GONE);
            }

            if (position % 3 == 0) {
                mItemView.setPadding(0, 0, ZYScreenUtils.dp2px(mContext, 7), ZYScreenUtils.dp2px(mContext, 11));
            } else if (position % 3 == 1) {
                mItemView.setPadding(ZYScreenUtils.dp2px(mContext, 4), 0, ZYScreenUtils.dp2px(mContext, 4), ZYScreenUtils.dp2px(mContext, 11));
            } else {
                mItemView.setPadding(ZYScreenUtils.dp2px(mContext, 7), 0, 0, ZYScreenUtils.dp2px(mContext, 11));
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mData.isDeleteStatus) {
                        listener.onHomeBookItemDel(mData, position);
                        return;
                    }
                    listener.onHomeBookItemClick(mData, position);
                }
            });

            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, data.getPic(), R.drawable.default_textbook, R.drawable.default_textbook);
            if (data.isFinished()) {
                data.setListener(null);
                textStatus.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                progressBgView.setVisibility(View.GONE);
            } else {
                data.setListener(downloadScriberListener);
                textStatus.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.VISIBLE);
                progressBgView.setVisibility(View.VISIBLE);
                float progress = (float) data.getCurrent() * 100.0f / (float) data.getTotal();
                progressView.setProgress((int) progress);
                textStatus.setText(mData.getStateString());
                if (data.getState().getState() != ZYDownState.PAUSE.getState()) {
                    ZYDownloadManager.getInstance().startDown(data);
                }

                textStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mData.isDeleteStatus) {
                            return;
                        }

                        if (mData.getState().getState() == ZYDownState.UNZIP.getState()) {
                            ZYToast.show(mContext, "解压中,请稍等!");
                            return;
                        }

                        if (mData.getState().getState() == ZYDownState.UNZIPERROR.getState()) {
                            unZip(mData);
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

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_home_book_item;
    }

    ZYDownloadScriberListener downloadScriberListener = new ZYDownloadScriberListener<SRBook>() {
        @Override
        public void onStart() {
            textStatus.setText(mData.getStateString());
        }

        @Override
        public void onComplete(final SRBook book) {
            ZYLog.e(getClass().getSimpleName(), "onComplete");

            textStatus.postDelayed(new Runnable() {
                @Override
                public void run() {
                    unZip(book);
                }
            }, 800);
        }

        @Override
        public void updateProgress(long current, long total) {
            float progress = (float) current * 100.0f / (float) total;
            textStatus.setText("下载中" + (int) progress + "%");
            progressView.setProgress((int) progress);
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

    private void unZip(final SRBook book) {
        book.setState(ZYDownState.UNZIP);
        book.update();
        textStatus.setText(mData.getStateString());
        SRBookFileManager.unZip(SRBookFileManager.getBookZipPath(book.book_id), SRBookFileManager.getBookPath(book.book_id), new SRBookFileManager.UnZipListener() {
            @Override
            public void unZipSuccess() {
                book.setState(ZYDownState.FINISH);
                book.setSavePath(SRBookFileManager.getBookPath(book.book_id));
                book.update();
                textStatus.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                progressBgView.setVisibility(View.GONE);
            }

            @Override
            public void unZipError() {
                book.setState(ZYDownState.UNZIPERROR);
                book.update();
                textStatus.setText(mData.getStateString());
                ZYToast.show(mContext, "如果多次解压失败,请删除后重新下载!");
            }
        });
    }


    public interface HomeBookItemListener {
        void onHomeBookItemClick(SRBook book, int position);

        void onHomeBookItemDel(SRBook book, int position);
    }
}
