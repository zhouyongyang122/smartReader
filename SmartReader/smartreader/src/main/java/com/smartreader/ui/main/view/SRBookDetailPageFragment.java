package com.smartreader.ui.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.smartreader.R;
import com.smartreader.SRApplication;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.smartreader.ui.main.model.SRPageManager;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.utils.ZYLog;
import com.smartreader.utils.ZYScreenUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/30.
 */

public class SRBookDetailPageFragment extends ZYBaseFragment implements View.OnClickListener {

    @Bind(R.id.layoutRoot)
    RelativeLayout layoutRoot;

    @Bind(R.id.imgBg)
    ImageView imgBg;

    private View sentenceSelBg;

    private ArrayList<View> sentenceBgs;

    private SRPage pageData;

    private double maxWidth = ZYScreenUtils.getScreenWidth(SRApplication.getInstance());

    private double maxHeight = 0;

    private BookDetailPageListener pageListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_book_detail_page, container, false);
        ButterKnife.bind(this, view);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
        float scale = 800.0f / 1200.0f;
        maxHeight = maxWidth / scale;
        layoutParams.height = (int) maxHeight;
        imgBg.setLayoutParams(layoutParams);

        showSentenceBgs(true);
        ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, pageData.getPicPath());
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

    private void showSentenceSelBg(RelativeLayout.LayoutParams layoutParams) {
        if (pageData == null) {
            return;
        }

        if (sentenceSelBg == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            sentenceSelBg = inflater.inflate(R.layout.sr_view_sentence_item, null);
            layoutRoot.addView(sentenceSelBg);
        }
        sentenceSelBg.setLayoutParams(layoutParams);
    }

    private void showSentenceBgs(boolean isShow) {
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
    }

    private RelativeLayout.LayoutParams getTractLayoutParams(SRTract tract) {
        double x = 0;
        double y = 0;
        double w = 0;
        double h = 0;
        x = tract.getTrack_left() * maxWidth;
        y = tract.getTrack_top() * maxHeight;
        w = (tract.getTrack_right() - tract.getTrack_left()) * maxWidth;
        h = (tract.getTrack_bottom() - tract.getTrack_top()) * maxHeight;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) w, (int) h);
        layoutParams.topMargin = (int) y;
        layoutParams.leftMargin = (int) x;
        return layoutParams;
    }


    public void setPageData(SRPage pageData) {
        this.pageData = pageData;
    }

    public void setPageListener(BookDetailPageListener pageListener) {
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

    public interface BookDetailPageListener {
        void onSelecteTrack(SRTract tract);

        boolean needShowSentenceBg();
    }
}
