package com.qudiandu.smartreader.ui.book.view.viewHolder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.utils.ZYScreenUtils;
import com.qudiandu.smartreader.utils.ZYToast;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/20.
 */

public class SRBooksDefItemVH extends ZYBaseViewHolder<SRBook> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.progressBgView)
    View progressBgView;

    @Bind(R.id.cardView)
    CardView cardView;

    SRBook mData;

    private SRBooksItemVH.HomeBookItemListener listener;

    public SRBooksDefItemVH(SRBooksItemVH.HomeBookItemListener listener) {
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
            mData = data;

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
                        ZYToast.show(mContext, "试用教材不可删除");
                        return;
                    }
                    if (listener != null) {
                        listener.onHomeBookItemClick(mData, position);
                    }
                }
            });
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_home_book_def_item;
    }
}
