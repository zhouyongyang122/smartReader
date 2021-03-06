package com.qudiandu.smartreader.ui.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.event.SREventSelectedBook;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.ui.main.activity.SRBookHomeActivity;
import com.qudiandu.smartreader.ui.main.activity.SRGradeActivity;
import com.qudiandu.smartreader.ui.main.contract.SRHomeContract;
import com.qudiandu.smartreader.ui.main.model.SRBookSelectManager;
import com.qudiandu.smartreader.ui.main.model.SRBookFileManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdert;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRHomeBannerVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRHomeBookVH;
import com.qudiandu.smartreader.ui.web.SRWebViewActivity;
import com.qudiandu.smartreader.utils.ZYLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/16.
 */

public class SRHomeFragment extends ZYBaseFragment<SRHomeContract.IPresenter> implements SRHomeContract.IView {

    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout layout_refresh;

    @Bind(R.id.scroll_view)
    NestedScrollView scroll_view;

    @Bind(R.id.layout_module_root)
    LinearLayout layout_module_root;

    SRHomeBookVH bookVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_main_home, container, false);
        ButterKnife.bind(this, view);
        layout_refresh.setEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showBook(SRBook book) {
        if (bookVH == null) {
            bookVH = new SRHomeBookVH();
            bookVH.bindView(LayoutInflater.from(mActivity).inflate(bookVH.getLayoutResId(), layout_module_root, false));
            layout_module_root.addView(bookVH.getItemView());
        }
        bookVH.updateView(book, 0);
    }

    @Override
    public void showAderts(List<SRAdert> aderts) {
        SRHomeBannerVH bannerVH = new SRHomeBannerVH(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        layout_refresh.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        layout_refresh.setEnabled(false);
                        break;
                }
                return false;
            }
        }, new SRHomeBannerVH.OnHomeBannerListener() {
            @Override
            public void onBanner(SRAdert slider) {
                mActivity.startActivity(SRWebViewActivity.createIntent(mActivity, slider.url, slider.title));
            }
        });

        bannerVH.bindView(LayoutInflater.from(mActivity).inflate(bannerVH.getLayoutResId(), layout_module_root, false));
        layout_module_root.addView(bannerVH.getItemView(), 0);
        bannerVH.updateView(aderts, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadBook();
    }
}
