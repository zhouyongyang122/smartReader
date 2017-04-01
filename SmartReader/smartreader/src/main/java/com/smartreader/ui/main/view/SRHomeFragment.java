package com.smartreader.ui.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smartreader.R;
import com.smartreader.base.event.SREventSelectedBook;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.ui.main.activity.SRBookDetailActivity;
import com.smartreader.ui.main.activity.SRGradeActivity;
import com.smartreader.ui.main.contract.SRHomeContract;
import com.smartreader.ui.main.model.SRAddBookManager;
import com.smartreader.ui.main.model.SRBookFileManager;
import com.smartreader.ui.main.model.bean.SRAdert;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.utils.ZYLog;

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
        mPresenter.loadBooks();
    }

    @Override
    public void showBooks(List<SRBook> books) {
        bookVH = new SRHomeBookVH(new SRHomeBookVH.HomeBookListener() {
            @Override
            public void onItemClick(SRBook book, int position) {
                if (book.getBook_id_int() < 0) {
                    mActivity.startActivity(SRGradeActivity.createIntent(mActivity));
                } else {
                    mActivity.startActivity(SRBookDetailActivity.createIntent(mActivity, book.savePath));
                }
            }
        });
        bookVH.bindView(LayoutInflater.from(mActivity).inflate(bookVH.getLayoutResId(), layout_module_root, false));
        layout_module_root.addView(bookVH.getItemView());
        bookVH.updateView(books, 0);
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

            }
        });

        bannerVH.bindView(LayoutInflater.from(mActivity).inflate(bannerVH.getLayoutResId(), layout_module_root, false));
        layout_module_root.addView(bannerVH.getItemView(), 0);
        bannerVH.updateView(aderts, 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBookSelected(SREventSelectedBook eventSelectedBook) {
        ZYLog.e(getClass().getSimpleName(), "eventBookSelected: " + SRAddBookManager.getInstance().getAddBooksSize());

        List<SRBook> books = SRAddBookManager.getInstance().getAddBooks();
        for (SRBook book : books) {
            book.setSavePath(SRBookFileManager.getBookZipPath(book.book_id));
            book.save();
        }
        mPresenter.getBooks().addAll(mPresenter.getBooks().size() - 1, books);
        bookVH.refreshData();
    }
}
