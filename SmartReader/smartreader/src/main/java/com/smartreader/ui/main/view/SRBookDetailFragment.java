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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.base.view.ZYRoudCornerRelativeLayout;
import com.smartreader.thirdParty.xunfei.XunFeiSDK;
import com.smartreader.ui.main.contract.SRBookDetailContract;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.main.model.bean.SRCatalogue;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.ui.mark.activity.SRMarkActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/29.
 */

public class SRBookDetailFragment extends ZYBaseFragment<SRBookDetailContract.IPresenter> implements SRBookDetailContract.IView, SRBookDetailPageFragment.BookDetailPageListener, SRBookDetailMenuVH.BookDetailMenuListener {

    @Bind(R.id.layoutRoot)
    RelativeLayout layoutRoot;

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

    private SRBookDetailMenuVH menuVH;

    private int curPageId = 1;

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
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPageId = position + 1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.imgBack, R.id.imgMenu, R.id.layout_score})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                mActivity.onBackPressed();
                break;
            case R.id.imgMenu:
                menuVH = new SRBookDetailMenuVH(this);
                menuVH.bindView(LayoutInflater.from(mActivity).inflate(menuVH.getLayoutResId(), null));
                layoutRoot.addView(menuVH.getItemView(), new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                int position = 0;
                for (SRCatalogue catalogue : mPresenter.getBookData().getCatalogue()) {
                    if (catalogue.getCatalogue_id() > 0 && curPageId == catalogue.getCatalogue_id()) {
                        menuVH.setDefSelPosition(position);
                    }
                    position++;
                }

                menuVH.updateView(mPresenter.getBookData().catalogue, 0);
                break;
            case R.id.layout_score:
                mActivity.startActivity(SRMarkActivity.createIntent(mActivity, mPresenter.getBookData().page.get(curPageId - 1), mPresenter.getBookData().book_id));
                break;
        }
    }

    @Override
    public void onSelecteTrack(SRTract tract) {
        if (!TextUtils.isEmpty(tract.getTrack_genre())) {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText(tract.getTrack_genre());
        } else {
            textTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMenuClose() {
        menuVH = null;
    }


    @Override
    public void onItemClick(SRCatalogue catalogue, int position) {
        curPageId = catalogue.getCatalogue_id();
        viewPage.setCurrentItem(curPageId - 1);
    }

    public boolean onBackPressed() {
        if (menuVH != null) {
            menuVH.close();
            return false;
        }
        return true;
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
