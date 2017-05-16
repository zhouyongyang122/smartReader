package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdert;
import com.qudiandu.smartreader.utils.ZYScreenUtils;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ZY on 17/3/27.
 */

public class SRHomeBannerVH extends ZYBaseViewHolder<List<SRAdert>> {

    @Bind(R.id.banner)
    ConvenientBanner<SRAdert> mBanner;

    private View.OnTouchListener mOnTouchListener;
    private OnHomeBannerListener mOnHomeBannerListener;

    private List<SRAdert> mBannerList;

    public SRHomeBannerVH(@NonNull View.OnTouchListener onTouchListener,
                          @NonNull OnHomeBannerListener onHomeBannerListener) {
        mOnTouchListener = onTouchListener;
        mOnHomeBannerListener = onHomeBannerListener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
        params.height = ZYScreenUtils.getScreenWidth(mContext) * 175 / 374;
        mBanner.setLayoutParams(params);
    }

    @Override
    public void updateView(List<SRAdert> data, int position) {
        if (data != null && !data.isEmpty()) {
            mBannerList = data;
            mItemView.setVisibility(View.VISIBLE);
            mBanner.setPages(new CBViewHolderCreator<ImageHolderView>() {
                @Override
                public ImageHolderView createHolder() {
                    return new ImageHolderView();
                }
            }, mBannerList)
                    .setPageIndicator(new int[]{R.drawable.sr_banner_indicator_normal, R.drawable.sr_banner_indicator_selected})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_LEFT)
                    .startTurning(3000);
            mBanner.getViewPager().setOnTouchListener(mOnTouchListener);
        } else {
            mItemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_home_banner;
    }

    public interface OnHomeBannerListener {

        void onBanner(SRAdert slider);
    }

    public class ImageHolderView implements Holder<SRAdert> {
        private ImageView imageView;
        private View view;

        @Override
        public View createView(Context context) {
            FrameLayout frameLayout = new FrameLayout(context);

            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
            view = new View(context);
            view.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundResource(value.resourceId);
            view.setClickable(true);

            frameLayout.addView(imageView);
            frameLayout.addView(view);

            return frameLayout;
        }

        @Override
        public void UpdateUI(Context context, final int position, final SRAdert data) {
            ZYImageLoadHelper.getImageLoader().loadImage(context, imageView, data.pic);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnHomeBannerListener.onBanner(mBannerList.get(position));
                }
            });
        }
    }
}
