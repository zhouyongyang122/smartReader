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
import com.smartreader.ZYPreferenceHelper;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.base.view.ZYRoudCornerRelativeLayout;
import com.smartreader.thirdParty.xunfei.XunFeiSDK;
import com.smartreader.ui.main.contract.SRBookDetailContract;
import com.smartreader.ui.main.model.SRPageManager;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.main.model.bean.SRCatalogue;
import com.smartreader.ui.main.model.bean.SRPage;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.ui.mark.activity.SRMarkActivity;
import com.smartreader.ui.mark.model.bean.SRMarkBean;
import com.smartreader.utils.ZYResourceUtils;
import com.smartreader.utils.ZYToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/29.
 */

public class SRBookDetailFragment extends ZYBaseFragment<SRBookDetailContract.IPresenter> implements SRBookDetailContract.IView, SRBookDetailPageFragment.BookDetailPageListener, SRBookDetailMenuVH.BookDetailMenuListener, SRPageManager.PagePlayListener, SRBookDetailSetVH.BookDetailSetListener {

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
    ZYRoudCornerRelativeLayout layout_score;

    @Bind(R.id.textScore)
    TextView textScore;

    @Bind(R.id.textScoreTip)
    TextView textScoreTip;

    @Bind(R.id.textSingle)
    TextView textSingle;

    @Bind(R.id.textRepeat)
    TextView textRepeat;

    @Bind(R.id.textSet)
    TextView textSet;

    @Bind(R.id.layoutRepeats)
    RelativeLayout layoutRepeats;

    @Bind(R.id.textStop)
    TextView textStop;

    @Bind(R.id.textPause)
    TextView textPause;

    @Bind(R.id.layoutSelTip)
    RelativeLayout layoutSelTip;

    @Bind(R.id.textSelTip)
    TextView textSelTip;

    @Bind(R.id.textSelCancle)
    TextView textSelCancle;

    private ArrayList<SRBookDetailPageFragment> pageFragments = new ArrayList<SRBookDetailPageFragment>();

    private BookDetailPageAdapter adapter;

    private SRBookDetailMenuVH menuVH;

    private SRBookDetailSetVH detailSetVH;

    private boolean isSelectingRepeats;

    private boolean isShowTrans;

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
                mPresenter.setCurPageId(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.imgBack, R.id.textSet, R.id.imgMenu, R.id.layout_score, R.id.textSingle, R.id.textRepeat, R.id.textStop, R.id.textPause, R.id.textSelCancle})
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
                    if (catalogue.getCatalogue_id() > 0 && mPresenter.getCurPageId() == catalogue.getCatalogue_id()) {
                        menuVH.setDefSelPosition(position);
                    }
                    position++;
                }

                menuVH.updateView(mPresenter.getBookData().catalogue, 0);
                break;
            case R.id.layout_score:
                mActivity.startActivity(SRMarkActivity.createIntent(mActivity, mPresenter.getBookData().page.get(mPresenter.getCurPageId() - 1), mPresenter.getBookData().book_id));
                if (mPresenter.isSingleRepeat()) {
                    mPresenter.stopSingleRepeat();
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
                textPause.setText("暂停");
                layoutBottomBar.setVisibility(View.GONE);
                isSelectingRepeats = true;
                SRPageManager.getInstance().setPagePlayListener(this);
                break;
            case R.id.textStop:
                mPresenter.stopRepeats();
                layoutBottomBar.setVisibility(View.VISIBLE);
                layoutRepeats.setVisibility(View.GONE);
                onTractPlayComplete(null);
                break;
            case R.id.textPause:
                if (textPause.getText().toString().equals("播放")) {
                    textPause.setText("暂停");
                    mPresenter.continueRepeats();
                } else {
                    textPause.setText("播放");
                    mPresenter.puaseRepeats();
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
                    detailSetVH = new SRBookDetailSetVH(this);
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
            onTractPlayComplete(null);
        } else {
            mPresenter.setSingleRepeat(true);
            ZYToast.show(mActivity, "单句复读模式已经开启!");
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

        SRMarkBean markBean = SRMarkBean.queryById(SRMarkBean.getMarkId(mPresenter.getBookData().book_id, tract.getPage_id() + "", tract.getTrack_id() + ""));
        refreshScore(markBean == null ? 0 : markBean.score);
    }

    private void refreshScore(int score) {
        if (score > 0) {
            textScore.setText(score + "");
            textScore.setBackgroundColor(ZYResourceUtils.getColor(R.color.c9));
            textScoreTip.setBackgroundColor(ZYResourceUtils.getColor(R.color.c7));

        } else {
            textScore.setText("练口语");
            textScore.setBackgroundColor(ZYResourceUtils.getColor(R.color.c7));
            textScoreTip.setBackgroundColor(ZYResourceUtils.getColor(R.color.c1));
        }
    }

    @Override
    public void onRepeatsTractPlay(SRTract tract) {
        int pageId = tract.getPage_id();
        if (mPresenter.getCurPageId() != pageId) {
            viewPage.setCurrentItem(pageId - 1);
            mPresenter.setCurPageId(pageId);
        }
        SRBookDetailPageFragment pageFragment = pageFragments.get(pageId - 1);
        pageFragment.onRepeatsTractPlay(tract);
    }

    @Override
    public void onTractPlayComplete(SRTract tract) {
        SRBookDetailPageFragment pageFragment = pageFragments.get(mPresenter.getCurPageId() - 1);
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
        mPresenter.setCurPageId(catalogue.getCatalogue_id());
        viewPage.setCurrentItem(mPresenter.getCurPageId() - 1);
    }

    @Override
    public void onTractTransChange(boolean show) {
        isShowTrans = show;
    }

    @Override
    public void onTractClickBgChange(boolean show) {

        int position = mPresenter.getCurPageId() - 1;

        SRBookDetailPageFragment pageFragment = pageFragments.get(position);
        pageFragment.showSentenceBgs(show);

        if (position - 1 >= 0) {
            pageFragment = pageFragments.get(position);
            pageFragment.showSentenceBgs(show);
        }

        if (position + 1 <= pageFragments.size() - 1) {
            pageFragment = pageFragments.get(mPresenter.getCurPageId());
            pageFragment.showSentenceBgs(show);
        }
    }

    @Override
    public void onTractSpeedChange(int speed) {

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
    }
}
