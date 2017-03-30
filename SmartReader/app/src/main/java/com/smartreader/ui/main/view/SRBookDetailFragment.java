package com.smartreader.ui.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.base.view.ZYRoudCornerRelativeLayout;
import com.smartreader.ui.main.contract.SRBookDetailContract;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/29.
 */

public class SRBookDetailFragment extends ZYBaseFragment<SRBookDetailContract.IPresenter> implements SRBookDetailContract.IView, SRBookDetailPageFragment.BookDetailPageListener {

    @Bind(R.id.viewPage)
    ViewPager viewPage;

    @Bind(R.id.imgBack)
    ImageView imgBack;

    @Bind(R.id.imgMenu)
    ImageView imgMenu;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.layout_score)
    ZYRoudCornerRelativeLayout layout_score;

    @Bind(R.id.textScore)
    TextView textScore;

    @Bind(R.id.textSpeed)
    TextView textSpeed;

    @Bind(R.id.textSingle)
    TextView textSingle;

    @Bind(R.id.textRepeat)
    TextView textRepeat;

    @Bind(R.id.textSet)
    TextView textSet;

    private ArrayList<SRBookDetailPageFragment> pageFragments = new ArrayList<SRBookDetailPageFragment>();

    private BookDetailPageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.sr_fragment_book_detail, container, false);
        ButterKnife.bind(this, viewGroup);
        return viewGroup;
    }

    @Override
    public void showBookData(SRBook bookData) {
        SRBookDetailPageFragment pageFragment;
        for (SRPage pageData : bookData.getPage()) {
            pageData.setLocalRootDirPath(mPresenter.getLocalRootDirPath());
            pageFragment = new SRBookDetailPageFragment();
            pageFragment.setPageData(pageData);
            pageFragment.setPageListener(this);
            pageFragments.add(pageFragment);
        }
        adapter = new BookDetailPageAdapter(((AppCompatActivity) mActivity).getSupportFragmentManager());
        viewPage.setOffscreenPageLimit(2);
        viewPage.setAdapter(adapter);
    }

    @OnClick({R.id.imgBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                mActivity.onBackPressed();
                break;
        }
    }

    @Override
    public void onSelecteTrack(SRTract tract) {
        if (TextUtils.isEmpty(tract.getTrack_genre())) {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText(tract.getTrack_genre());
        } else {
            textTitle.setVisibility(View.GONE);
        }
    }

    private class BookDetailPageAdapter extends FragmentPagerAdapter {

        public BookDetailPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return pageFragments.get(position);
        }

        @Override
        public int getCount() {
            return pageFragments.size();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }
}
