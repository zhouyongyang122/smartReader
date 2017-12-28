package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookSelectItemVH extends ZYBaseViewHolder<SRBook> {

    @Bind(R.id.cardView)
    CardView cardView;

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.imgCheck)
    ImageView imgCheck;

    @Bind(R.id.textTitle)
    TextView textTitle;

    BookListItemListener listener;

    SRBook mData;

    boolean isTaskSel;

    public SRBookSelectItemVH(BookListItemListener listener, boolean isTaskSel) {
        this.listener = listener;
        this.isTaskSel = isTaskSel;
    }

    @Override
    public void findView(View view) {
        super.findView(view);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
        float scale = 105.0f / 155.0f;
        float width = ZYScreenUtils.getScreenWidth(SRApplication.getInstance()) - ZYScreenUtils.dp2px(SRApplication.getInstance(), 15 + 15);
        width = width - ZYScreenUtils.dp2px(SRApplication.getInstance(), 11 + 11);
        width = width / 3.0f;
        float height = width / scale;

        layoutParams.height = (int) height;

        imgBg.setLayoutParams(layoutParams);
    }

    @Override
    public void updateView(SRBook data, int position) {
        if (data != null) {
            mData = data;

            if (position % 3 == 0) {
                mItemView.setPadding(0, ZYScreenUtils.dp2px(mContext, 15), ZYScreenUtils.dp2px(mContext, 7), 0);
            } else if (position % 3 == 1) {
                mItemView.setPadding(ZYScreenUtils.dp2px(mContext, 4), ZYScreenUtils.dp2px(mContext, 15), ZYScreenUtils.dp2px(mContext, 4), 0);
            } else {
                mItemView.setPadding(ZYScreenUtils.dp2px(mContext, 7), ZYScreenUtils.dp2px(mContext, 15), 0, 0);
            }

            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, data.getPic(), R.color.c5, R.color.c5);
            textTitle.setText(data.getName() + ":");

            if (!isTaskSel) {
                imgCheck.setVisibility(View.VISIBLE);
                check(mData.isCheck);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mData.isCheck = !mData.isCheck;
                        check(mData.isCheck);
                        listener.onBookItemSelect(mData, mData.isCheck);
                    }
                });
            }else {
                cardView.setClickable(false);
            }
        }
    }

    private void check(boolean checked) {
        if (checked) {
            imgCheck.setBackgroundResource(R.drawable.icon_selected);
        } else {
            imgCheck.setBackgroundResource(R.drawable.icon_notselected);
        }
    }


    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_book_list_item;
    }

    public interface BookListItemListener {
        void onBookItemSelect(SRBook book, boolean isSelected);
    }
}
