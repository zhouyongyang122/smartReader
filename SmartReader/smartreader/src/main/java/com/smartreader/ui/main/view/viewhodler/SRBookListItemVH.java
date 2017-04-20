package com.smartreader.ui.main.view.viewhodler;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.SRApplication;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.utils.ZYScreenUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookListItemVH extends ZYBaseViewHolder<SRBook> {

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

    public SRBookListItemVH(BookListItemListener listener) {
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
    public void updateView(SRBook data, int position) {
        if (data != null) {
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, data.getPic(), R.drawable.default_textbook, R.drawable.default_textbook);
            textTitle.setText(data.getName() + ":");
            check(mData.isCheck);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mData.isCheck = !mData.isCheck;
                    check(mData.isCheck);
                    listener.onBookItemSelect(mData, mData.isCheck);
                }
            });
        }
    }

    private void check(boolean checked) {
        if (checked) {
            imgCheck.setBackgroundResource(R.drawable.sr_bg_corner360dp_boder1dpc4_solidc13);
        } else {
            imgCheck.setBackgroundResource(R.drawable.sr_bg_corner360dp_boder1dpc2_solidwhite);
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
