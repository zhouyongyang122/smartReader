package com.qudiandu.smartdub.ui.myAudio.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.SRApplication;
import com.qudiandu.smartdub.ZYPreferenceHelper;
import com.qudiandu.smartdub.base.mvp.ZYBaseFragment;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueDetail;
import com.qudiandu.smartdub.utils.ZYLog;
import com.qudiandu.smartdub.utils.ZYScreenUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCatalogueDetailPageFragment extends ZYBaseFragment implements View.OnClickListener {

    @Bind(R.id.layoutRoot)
    RelativeLayout layoutRoot;

    @Bind(R.id.imgBg)
    ImageView imgBg;

    private View sentenceSelBg;

    private ArrayList<View> sentenceBgs;

    private SRCatalogueDetail.PageNew pageData;

    private double maxWidth = ZYScreenUtils.getScreenWidth(SRApplication.getInstance());

    private double maxHeight = 0;

    private CatalogueDetailPageListener pageListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_catalogue_detail_page, container, false);
        ButterKnife.bind(this, view);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
        float scale = 800.0f / 1200.0f;
        maxHeight = maxWidth / scale;
        layoutParams.height = (int) maxHeight;
        imgBg.setLayoutParams(layoutParams);

        showSentenceBgs(ZYPreferenceHelper.getInstance().isShowTractBg());
        ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, pageData.getPage_url(), R.color.c2, R.color.c2);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (sentenceBgs != null) {
            sentenceBgs.clear();
            sentenceBgs = null;
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSelBg();
    }

    public void hideSelBg() {
        if (sentenceSelBg != null) {
            sentenceSelBg.setVisibility(View.GONE);
        }
    }

    private void showSentenceSelBg(RelativeLayout.LayoutParams layoutParams) {
        if (pageData == null) {
            return;
        }

        if (sentenceSelBg == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            sentenceSelBg = inflater.inflate(R.layout.sr_view_sentence_item, null);
            layoutRoot.addView(sentenceSelBg);
        }
        sentenceSelBg.setVisibility(View.VISIBLE);
        sentenceSelBg.setLayoutParams(layoutParams);
    }

    public void showSentenceBgs(boolean isShow) {
        try {
            if (pageData == null) {
                return;
            }
            if (sentenceBgs == null) {
                sentenceBgs = new ArrayList<View>();
                View view = null;
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                RelativeLayout.LayoutParams layoutParams;
                int position = 0;
                for (SRTract tract : pageData.getTrack()) {
                    view = inflater.inflate(R.layout.sr_view_sentence_item, null);
                    view.setLayoutParams(getTractLayoutParams(tract));
                    view.setTag("" + position);
                    view.setOnClickListener(this);
                    if (isShow) {
                        view.setBackgroundResource(R.drawable.sr_bg_rect_transparent40_c10);
                    } else {
                        view.setBackgroundResource(R.color.transparent);
                    }
                    layoutRoot.addView(view);
                    sentenceBgs.add(view);
                    position++;
                }
            } else {
                for (View view : sentenceBgs) {
                    if (isShow) {
                        view.setBackgroundResource(R.drawable.sr_bg_rect_transparent40_c10);
                    } else {
                        view.setBackgroundResource(R.color.transparent);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private RelativeLayout.LayoutParams getTractLayoutParams(SRTract tract) {
        double x = 0;
        double y = 0;
        double w = 0;
        double h = 0;
        x = tract.getTrack_left() * maxWidth - 10;
        y = tract.getTrack_top() * maxHeight - 10;
        w = (tract.getTrack_right() - tract.getTrack_left()) * maxWidth + 20;
        h = (tract.getTrack_bottom() - tract.getTrack_top()) * maxHeight + 20;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) w, (int) h);
        layoutParams.topMargin = (int) y;
        layoutParams.leftMargin = (int) x;
        return layoutParams;
    }


    public void setPageData(SRCatalogueDetail.PageNew pageData) {
        this.pageData = pageData;
    }

    public void setPageListener(CatalogueDetailPageListener pageListener) {
        this.pageListener = pageListener;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() != null) {
            try {
                ZYLog.e(getClass().getSimpleName(), "onClick: " + view.getTag().toString());
                int position = Integer.valueOf(view.getTag().toString()).intValue();
                SRTract tract = pageData.getTrack().get(position);
                showSentenceSelBg((RelativeLayout.LayoutParams) view.getLayoutParams());
                pageListener.onSelecteTrack(tract);
            } catch (Exception e) {
                ZYLog.e(getClass().getSimpleName(), "onClick-error: " + e.getMessage());
            }
        }
    }

    public void onRepeatsTractPlay(SRTract tract) {
        try {
            showSentenceSelBg(getTractLayoutParams(tract));
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "onTractPlay-error: " + e.getMessage());
        }
    }

    public interface CatalogueDetailPageListener {
        void onSelecteTrack(SRTract tract);

        boolean needShowSentenceBg();
    }
}
