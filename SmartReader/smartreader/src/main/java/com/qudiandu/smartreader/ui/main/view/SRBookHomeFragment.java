package com.qudiandu.smartreader.ui.main.view;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.ZYPreferenceHelper;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.ui.main.contract.SRBookHomeContract;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRCatalogue;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRBookHomeMenuVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRBookHomeSetVH;
import com.qudiandu.smartreader.ui.mark.activity.SRMarkActivity;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/29.
 */

public class SRBookHomeFragment extends ZYBaseFragment<SRBookHomeContract.IPresenter> implements SRBookHomeContract.IView, SRBookPageFragment.BookDetailPageListener, SRBookHomeMenuVH.BookDetailMenuListener, SRPlayManager.PagePlayListener, SRBookHomeSetVH.BookDetailSetListener {

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

    @Bind(R.id.layoutBottomBar)
    RelativeLayout layoutBottomBar;

    @Bind(R.id.layout_score)
    LinearLayout layout_score;

    @Bind(R.id.textSingle)
    TextView textSingle;

    @Bind(R.id.textRepeat)
    TextView textRepeat;

    @Bind(R.id.textSet)
    TextView textSet;

    @Bind(R.id.layoutRepeats)
    RelativeLayout layoutRepeats;

    @Bind(R.id.imgStop)
    ImageView imgStop;

    @Bind(R.id.imgPause)
    ImageView imgPause;

    @Bind(R.id.layoutSelTip)
    RelativeLayout layoutSelTip;

    @Bind(R.id.textSelTip)
    TextView textSelTip;

    @Bind(R.id.textSelCancle)
    TextView textSelCancle;

    private ArrayList<SRBookPageFragment> pageFragments = new ArrayList<SRBookPageFragment>();

    private BookDetailPageAdapter adapter;

    private SRBookHomeMenuVH menuVH;

    private SRBookHomeSetVH detailSetVH;

    private boolean isSelectingRepeats;

    private boolean isShowTrans;

    boolean isPuase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.sr_fragment_book_detail, container, false);
        ButterKnife.bind(this, viewGroup);
        isShowTrans = ZYPreferenceHelper.getInstance().isShowTractTrans();
        return viewGroup;
    }

    @Override
    public void showBookData(SRBook bookData) {
        SRBookPageFragment pageFragment;
        for (SRPage pageData : bookData.getPage()) {
            pageData.setLocalRootDirPath(mPresenter.getLocalRootDirPath());
            pageFragment = new SRBookPageFragment();
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
//                mPresenter.setCurPageId(position + 1);
                refreshScore();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPage.setCurrentItem(mPresenter.getBookData().lastPageIndex);
    }

    @OnClick({R.id.imgBack, R.id.textSet, R.id.imgMenu, R.id.layout_score, R.id.textSingle, R.id.textRepeat, R.id.imgStop, R.id.imgPause, R.id.textSelCancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                mActivity.onBackPressed();
                break;
            case R.id.imgMenu: {
                menuVH = new SRBookHomeMenuVH(this);
                menuVH.bindView(LayoutInflater.from(mActivity).inflate(menuVH.getLayoutResId(), null));
                layoutRoot.addView(menuVH.getItemView(), new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                int position = 0;
                SRPage page = mPresenter.getBookData().page.get(viewPage.getCurrentItem());
                for (SRCatalogue catalogue : mPresenter.getBookData().getCatalogue()) {
                    if (catalogue.getCatalogue_id() > 0 && page != null && page.getCatalogueId() == catalogue.getCatalogue_id()) {
                        menuVH.setDefSelPosition(position);
                    }
                    position++;
                }

                menuVH.updateView(mPresenter.getBookData().catalogue, 0);
            }
            break;
            case R.id.layout_score: {
                SRPage page = mPresenter.getBookData().page.get(viewPage.getCurrentItem());
                mActivity.startActivity(SRMarkActivity.createIntent(mActivity, mPresenter.getTractsByCatalogueId(page.getCatalogueId()), mPresenter.getBookData().book_id, page.getCatalogueId() + ""));
                if (mPresenter.isSingleRepeat()) {
                    mPresenter.stopSingleRepeat();
                }
            }
            break;
            case R.id.textSingle:
                singleTractClick();
                break;
            case R.id.textRepeat:
                if (mPresenter.isSingleRepeat()) {
                    mPresenter.stopSingleRepeat();
                }
                mPresenter.setRepeats(true);
                layoutSelTip.setVisibility(View.VISIBLE);
                textSelTip.setText("请点击选择复读起点区域");
                imgPause.setImageResource(R.drawable.cq_icon_suspend);
                isPuase = false;
                layoutBottomBar.setVisibility(View.GONE);
                isSelectingRepeats = true;
                SRPlayManager.getInstance().setPagePlayListener(this);
                break;
            case R.id.imgStop:
                mPresenter.stopRepeats();
                layoutBottomBar.setVisibility(View.VISIBLE);
                layoutRepeats.setVisibility(View.GONE);
                onTractPlayComplete(null);
                break;
            case R.id.imgPause:
                if (isPuase) {
                    imgPause.setImageResource(R.drawable.cq_icon_suspend);
                    isPuase = false;
                    mPresenter.continueRepeats();
                } else {
                    imgPause.setImageResource(R.drawable.cq_icon_play);
                    mPresenter.puaseRepeats();
                    isPuase = true;
                }
                break;
            case R.id.textSelCancle:
                mPresenter.stopRepeats();
                layoutBottomBar.setVisibility(View.VISIBLE);
                layoutRepeats.setVisibility(View.GONE);
                layoutSelTip.setVisibility(View.GONE);
                break;
            case R.id.textSet:
                if (detailSetVH == null) {
                    detailSetVH = new SRBookHomeSetVH(this);
                    detailSetVH.attachTo(layoutRoot);
                }
                detailSetVH.show();
                break;
        }
    }

    private void singleTractClick() {
        if (mPresenter.isSingleRepeat()) {
            mPresenter.stopSingleRepeat();
            ZYToast.show(mActivity, "单句复读模式已经关闭!");
            textSingle.setText("单句");
            onTractPlayComplete(null);
        } else {
            mPresenter.setSingleRepeat(true);
            ZYToast.show(mActivity, "单句复读模式已经开启!");
            textSingle.setText("连续");
        }
    }

    @Override
    public void onSelecteTrack(SRTract tract) {
        if (isShowTrans && !isSelectingRepeats && !TextUtils.isEmpty(tract.getTrack_genre())) {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText(tract.getTrack_genre());
        } else {
            textTitle.setVisibility(View.GONE);
        }

        mPresenter.onSelecteTrack(tract);
    }

    private void refreshScore(int score) {
//        if (score > 0) {
//            textScore.setText(score + "");
//            textScore.setBackgroundColor(ZYResourceUtils.getColor(R.color.c9));
//            textScoreTip.setBackgroundColor(ZYResourceUtils.getColor(R.color.c7));
//
//        } else {
//            textScore.setText("配音");
//            textScore.setBackgroundColor(ZYResourceUtils.getColor(R.color.c7));
//            textScoreTip.setBackgroundColor(ZYResourceUtils.getColor(R.color.c1));
//        }
    }

    @Override
    public void onRepeatsTractPlay(SRTract tract) {
        int pageId = tract.getPage_id();
        SRPage page = mPresenter.getBookData().page.get(viewPage.getCurrentItem());
        if (page.getPage_id() != pageId) {
            viewPage.setCurrentItem(getPageIndex(pageId));
        }
        SRBookPageFragment pageFragment = pageFragments.get(viewPage.getCurrentItem());
        pageFragment.onRepeatsTractPlay(tract);
    }

    @Override
    public void onTractPlayComplete(SRTract tract) {
        SRBookPageFragment pageFragment = pageFragments.get(viewPage.getCurrentItem());
        pageFragment.hideSelBg();
    }

    @Override
    public void selRepeatsEnd() {
        textSelTip.setText("请点击选择复读终点区域");
    }

    @Override
    public void playRepeats() {
        isSelectingRepeats = false;
        layoutSelTip.setVisibility(View.GONE);
        layoutRepeats.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean needShowSentenceBg() {
        return mPresenter.isNeedShowSentenceBg();
    }

    @Override
    public void onMenuClose() {
        menuVH = null;
    }


    @Override
    public void onItemClick(SRCatalogue catalogue, int position) {
        int index = getPageIndex(catalogue.getFristPageId());
        viewPage.setCurrentItem(index);
    }

    @Override
    public void onTractTransChange(boolean show) {
        isShowTrans = show;
    }

    @Override
    public void onTractClickBgChange(boolean show) {

        int position = viewPage.getCurrentItem();

        SRBookPageFragment pageFragment = pageFragments.get(position);
        pageFragment.showSentenceBgs(show);

        if (position - 1 >= 0) {
            pageFragment = pageFragments.get(position - 1);
            pageFragment.showSentenceBgs(show);
        }

        if (position + 1 < pageFragments.size()) {
            pageFragment = pageFragments.get(position + 1);
            pageFragment.showSentenceBgs(show);
        }
    }

    @Override
    public void onTractSpeedChange(int speed) {
        int speed_ = speed - 50;
    }

    public boolean onBackPressed() {
        if (menuVH != null) {
            menuVH.close();
            return false;
        }

        if (detailSetVH != null && detailSetVH.isVisibility()) {
            detailSetVH.hide();
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

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter.isRepeats()) {
            mPresenter.puaseRepeats();
        } else if (mPresenter.isSingleRepeat()) {
            mPresenter.stopSingleRepeat();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter.isRepeats()) {
            mPresenter.continueRepeats();
        }
        viewPage.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshScore();
            }
        }, 500);

    }

    private void refreshScore() {
        try {
            int score = 0;
            SRPage page = mPresenter.getBookData().page.get(viewPage.getCurrentItem());
            if (refreshScoreLayoutVisable(page)) {
                SRMarkBean markBean = null;
                for (SRTract tract : page.getTrack()) {
                    markBean = SRMarkBean.queryById(SRMarkBean.getMarkId(mPresenter.getBookData().book_id, tract.getPage_id() + "", tract.getTrack_id() + ""));
                    if (markBean.score <= 0) {
                        refreshScore(0);
                        break;
                    } else {
                        score += markBean.score;
                    }
                }

                refreshScore(score / page.getTrack().size());
            }
        } catch (Exception e) {
            refreshScore(0);
        }
    }

    private boolean refreshScoreLayoutVisable(SRPage page) {
        if (page.getTrack().size() <= 0) {
            layout_score.setVisibility(View.GONE);
            return false;
        } else {
            SRTract tract = page.getTrack().get(0);
            if (TextUtils.isEmpty(tract.getTrack_txt())) {
                layout_score.setVisibility(View.GONE);
                return false;
            } else {
                layout_score.setVisibility(View.VISIBLE);
                return true;
            }
        }
    }

    /**
     * 根据pageId获取界面index
     *
     * @param pageId
     * @return
     */
    private int getPageIndex(int pageId) {
        int index = 0;
        try {
            List<SRPage> pages = mPresenter.getBookData().getPage();
            for (SRPage page : pages) {
                if (page.getPage_id() == pageId) {
                    break;
                }
                index++;
            }
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "getPageIndex-error:" + e.getMessage());
        }
        return index;
    }

    @Override
    public void onDestroyView() {

        try {
            SRBook book = SRBook.queryById(mPresenter.getBookData().book_id);
            book.lastPageIndex = viewPage.getCurrentItem();
            book.update();
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "onDestroyView-error:" + e.getMessage());
        }

        super.onDestroyView();
    }
}
