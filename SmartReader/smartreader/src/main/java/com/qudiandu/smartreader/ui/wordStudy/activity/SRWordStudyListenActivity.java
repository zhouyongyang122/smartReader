package com.qudiandu.smartreader.ui.wordStudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYFragmentAdapter;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.ui.main.model.SRPlayManager;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyWord;
import com.qudiandu.smartreader.ui.wordStudy.view.SRWordStudyListenFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyListenActivity extends ZYBaseActivity {

    @Bind(R.id.viewPage)
    ViewPager viewPage;

    ZYFragmentAdapter adapter;

    int mIndex = 0;

    public static Intent createIntent(Context context, ArrayList<SRWordStudyWord> words) {
        Intent intent = new Intent(context, SRWordStudyListenActivity.class);
        intent.putExtra("words", words);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_wrod_study_listen);
        ButterKnife.bind(this);

        final ArrayList<SRWordStudyWord> words = (ArrayList<SRWordStudyWord>) getIntent().getSerializableExtra("words");
        adapter = new ZYFragmentAdapter(getSupportFragmentManager());
        SRWordStudyListenFragment listenFragment;
        int index = 0;
        for (SRWordStudyWord srWordStudyWord : words) {
            listenFragment = new SRWordStudyListenFragment();
            listenFragment.setWord(srWordStudyWord,index);
            if (index >= words.size() - 1) {
                listenFragment.setIsLastIndex(true);
            }
            listenFragment.setListener(new SRWordStudyListenFragment.WordStudyListener() {
                @Override
                public void onNext() {
                    viewPage.setCurrentItem(++mIndex);
                }

                @Override
                public void onFinished() {
                    finish();
                }
            });
            adapter.addFragment(listenFragment, "");
            index++;
        }
        viewPage.setAdapter(adapter);

        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mActionBar.showTitle((position + 1) + "/" + words.size());
                SRWordStudyListenFragment fragment = (SRWordStudyListenFragment) adapter.getItem(mIndex);
                fragment.showSoftInput();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SRPlayManager.getInstance().stopAudio();
    }
}