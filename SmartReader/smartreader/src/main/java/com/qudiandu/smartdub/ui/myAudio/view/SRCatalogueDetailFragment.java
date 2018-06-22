package com.qudiandu.smartdub.ui.myAudio.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.mvp.ZYBaseFragment;
import com.qudiandu.smartdub.base.view.ZYLoadingView;
import com.qudiandu.smartdub.thirdParty.image.ZYIImageLoader;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.main.model.SRPlayManager;
import com.qudiandu.smartdub.ui.main.model.bean.SRTract;
import com.qudiandu.smartdub.ui.myAudio.contract.SRCatalogueDetailContract;
import com.qudiandu.smartdub.ui.myAudio.model.SRCatalogueDetail;
import com.qudiandu.smartdub.utils.SRShareUtils;
import com.qudiandu.smartdub.utils.ZYLog;
import com.qudiandu.smartdub.utils.ZYToast;
import com.third.loginshare.entity.ShareEntity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCatalogueDetailFragment extends ZYBaseFragment<SRCatalogueDetailContract.IPresenter> implements SRCatalogueDetailContract.IView, SRCatalogueDetailPageFragment.CatalogueDetailPageListener, SRPlayManager.PagePlayListener {

    @Bind(R.id.layoutRoot)
    RelativeLayout layoutRoot;

    @Bind(R.id.viewPage)
    ViewPager viewPage;

    @Bind(R.id.imgShare)
    ImageView imgShare;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textPre)
    TextView textPre;

    @Bind(R.id.textNext)
    TextView textNext;

    @Bind(R.id.textPlay)
    TextView textPlay;

    ZYLoadingView loadingView;

    private boolean autoPlayStatus;

    private ArrayList<SRCatalogueDetailPageFragment> pageFragments = new ArrayList<SRCatalogueDetailPageFragment>();

    private CatalogueDetailPageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.sr_fragment_catalogue_detail, container, false);
        ButterKnife.bind(this, viewGroup);
        initLoadingView();
        mPresenter.loadData();
        SRPlayManager.getInstance().setPagePlayListener(this);
        return viewGroup;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initLoadingView() {
        loadingView = new ZYLoadingView(mActivity);
        loadingView.attach(layoutRoot);
        loadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.showLoading();
                mPresenter.loadData();
            }
        });
    }

    private int findIndexByPageId(int pageId) {
        int index = 0;
        try {
            for (SRCatalogueDetail.PageNew page : mPresenter.getCatalogueDetail().getPage()) {
                if (page.getPage_id() == pageId) {
                    break;
                }
                index++;
            }
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "findIndexByPageId-error:" + e.getMessage());
        }
        return index;
    }

    public void showCatalogueData(SRCatalogueDetail data) {
        SRCatalogueDetailPageFragment pageFragment;
        pageFragments.clear();
        for (SRCatalogueDetail.PageNew pageData : data.getPage()) {
            pageFragment = new SRCatalogueDetailPageFragment();
            pageFragment.setPageData(pageData);
            pageFragment.setPageListener(this);
            pageFragments.add(pageFragment);
        }
        adapter = new CatalogueDetailPageAdapter(((AppCompatActivity) mActivity).getSupportFragmentManager());
        viewPage.setOffscreenPageLimit(2);
        viewPage.setAdapter(adapter);
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showOrHideNextAndPre(position, pageFragments.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        showOrHideNextAndPre(0, pageFragments.size());

        autoPlayOrStop();
    }

    @Override
    public void onSelecteTrack(SRTract tract) {
        if (autoPlayStatus) {
            return;
        }
        SRPlayManager.getInstance().setPagePlayListener(this);
        textTitle.setVisibility(View.VISIBLE);
        textTitle.setText(tract.getTrack_genre());
        SRPlayManager.getInstance().startAudio(mPresenter.getCatalogueDetail().getAudioPath(), tract.getAudioStart(), tract.getAudioEnd());
    }

    @Override
    public boolean needShowSentenceBg() {
        return true;
    }

    private void showOrHideNextAndPre(int index, int size) {

        if (index <= 0) {
            textPre.setVisibility(View.GONE);
        } else {
            textPre.setVisibility(View.VISIBLE);
        }

        if (index >= size - 1) {
            textNext.setVisibility(View.GONE);
        } else {
            textNext.setVisibility(View.VISIBLE);
        }
    }

    private void autoPlayOrStop() {
        if (!autoPlayStatus) {
            SRCatalogueDetail.PageNew pageData = null;
            ArrayList<SRTract> tracts = new ArrayList<SRTract>();
            for (int i = viewPage.getCurrentItem(); i < pageFragments.size(); i++) {
                pageData = mPresenter.getCatalogueDetail().getPage().get(i);
                tracts.addAll(pageData.getTrack());
            }

            if (tracts.size() <= 0) {
                ZYToast.show(mActivity, "没有可播放的句子!");
            } else {
                autoPlayStatus = true;
                textPlay.setText("暂停");
                textPre.setEnabled(false);
                textNext.setEnabled(false);
                viewPage.setEnabled(false);
                for (SRTract tract : tracts) {
                    tract.setMp3Path(mPresenter.getCatalogueDetail().getAudioPath());
                }
                SRPlayManager.getInstance().startRepeats(tracts, false);
            }
        } else {
            autoPlayStatus = false;
            SRPlayManager.getInstance().stopAudio();
            textPlay.setText("播放");
            textPre.setEnabled(true);
            textNext.setEnabled(true);
            viewPage.setEnabled(true);
        }
    }

    @OnClick({R.id.imgBack, R.id.imgShare, R.id.textPre, R.id.textNext, R.id.textPlay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                mActivity.onBackPressed();
                break;
            case R.id.imgShare: {
                ZYImageLoadHelper.getImageLoader().loadFromMediaStore(this, SRUserManager.getInstance().getUser().avatar, new ZYIImageLoader.OnLoadLocalImageFinishListener() {
                    @Override
                    public void onLoadFinish(@Nullable final Bitmap bitmap) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShareEntity shareEntity = new ShareEntity();
                                shareEntity.avatarUrl = SRUserManager.getInstance().getUser().avatar;
                                if (bitmap != null) {
                                    shareEntity.avatarBitmap = bitmap;
                                } else {
                                    shareEntity.avatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                }
                                shareEntity.webUrl = mPresenter.getCatalogueDetail().getShare_url();
                                shareEntity.title = SRUserManager.getInstance().getUser().nickname + " 同学的课文配音作品快来听一下吧！!";
                                shareEntity.text = "专为小学设计的英语听说训练智能学习工具";
                                new SRShareUtils(mActivity, shareEntity).share();
                            }
                        });
                    }
                });
            }
            break;
            case R.id.textPre: {
                int pageIndex = viewPage.getCurrentItem();
                viewPage.setCurrentItem(--pageIndex);
                showOrHideNextAndPre(pageIndex, pageFragments.size());
            }
            break;
            case R.id.textNext: {
                int pageIndex = viewPage.getCurrentItem();
                viewPage.setCurrentItem(++pageIndex);
                showOrHideNextAndPre(pageIndex, pageFragments.size());
            }
            break;
            case R.id.textPlay: {
                SRPlayManager.getInstance().setPagePlayListener(this);
                autoPlayOrStop();
            }
            break;
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loadingView.showNothing();
    }

    @Override
    public void showError() {
        loadingView.showError();
    }

    @Override
    public void onRepeatsTractPlay(SRTract tract) {
        int pageId = tract.getPage_id();
        SRCatalogueDetail.PageNew page = mPresenter.getCatalogueDetail().getPage().get(viewPage.getCurrentItem());
        if (pageId != page.getPage_id()) {
            viewPage.setCurrentItem(findIndexByPageId(pageId));
        }
        SRCatalogueDetailPageFragment pageFragment = pageFragments.get(viewPage.getCurrentItem());
        pageFragment.onRepeatsTractPlay(tract);
        textTitle.setVisibility(View.GONE);
    }

    @Override
    public void onTractPlayComplete(SRTract tract) {
        if (autoPlayStatus) {
            autoPlayOrStop();
        }
        SRCatalogueDetailPageFragment pageFragment = pageFragments.get(viewPage.getCurrentItem());
        pageFragment.hideSelBg();
        textTitle.setVisibility(View.GONE);
    }

    private class CatalogueDetailPageAdapter extends FragmentPagerAdapter {

        public CatalogueDetailPageAdapter(FragmentManager fm) {
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
}
