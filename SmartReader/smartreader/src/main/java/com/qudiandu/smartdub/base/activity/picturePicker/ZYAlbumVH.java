package com.qudiandu.smartdub.base.activity.picturePicker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;

import butterknife.Bind;

public class ZYAlbumVH extends ZYBaseViewHolder<ZYAlbum> {

    @Bind(R.id.img_cover)
    ImageView mImgCover;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_count)
    TextView mTvCount;
    @Bind(R.id.img_selected)
    ImageView mImgSelected;

    @Override
    public void updateView(ZYAlbum data, int position) {
        if (data != null) {
            ZYImageLoadHelper.getImageLoader().loadImage(mContext, mImgCover, data.coverPath);
            mImgSelected.setVisibility(data.isSelected ? View.VISIBLE : View.GONE);
            mTvName.setText(data.name);
            mTvCount.setText(data.pictures.size() + "张");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_item_picture_album;
    }
}
