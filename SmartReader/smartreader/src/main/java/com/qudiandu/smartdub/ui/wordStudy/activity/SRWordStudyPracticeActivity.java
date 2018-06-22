package com.qudiandu.smartdub.ui.wordStudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.adapter.ZYFragmentAdapter;
import com.qudiandu.smartdub.base.mvp.ZYBaseActivity;
import com.qudiandu.smartdub.base.view.ZYNoScollViewPager;
import com.qudiandu.smartdub.ui.main.model.SRPlayManager;
import com.qudiandu.smartdub.ui.wordStudy.model.bean.SRWordStudyWord;
import com.qudiandu.smartdub.ui.wordStudy.view.SRWordStudyPracticeFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/12/12.
 */

public class SRWordStudyPracticeActivity extends ZYBaseActivity {

    @Bind(R.id.viewPage)
    ZYNoScollViewPager viewPage;

    int curPosition;

    ArrayList<SRWordStudyWord> mWords;

    public static Intent createIntent(Context context, ArrayList<SRWordStudyWord> words) {
        Intent intent = new Intent(context, SRWordStudyPracticeActivity.class);
        intent.putExtra("words", words);
        return intent;
    }

    public static Intent createIntent(Context context, ArrayList<SRWordStudyWord> words, boolean isPicType) {
        Intent intent = new Intent(context, SRWordStudyPracticeActivity.class);
        intent.putExtra("words", words);
        intent.putExtra("isPicType", isPicType);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_wrod_study_listen);
        ButterKnife.bind(this);

        mWords = (ArrayList<SRWordStudyWord>) getIntent().getSerializableExtra("words");

        mActionBar.showTitle("1/" + mWords.size());

        ZYFragmentAdapter adapter = new ZYFragmentAdapter(getSupportFragmentManager());
        SRWordStudyPracticeFragment listenFragment;
        int index = 0;
        for (SRWordStudyWord srWordStudyWord : mWords) {
            listenFragment = new SRWordStudyPracticeFragment();
            listenFragment.setWord(srWordStudyWord);
            listenFragment.setIsPicType(getIntent().getBooleanExtra("isPicType", false));
            listenFragment.setListener(new SRWordStudyPracticeFragment.WordStudyPracticeFragmentListener() {
                @Override
                public void onFinishedAnswer() {
                    startActivity(SRWordStudyPracticeActivity.createIntent(SRWordStudyPracticeActivity.this, mWords, true));
                    finish();
                }

                @Override
                public void onNextAnswer() {
                    viewPage.setCurrentItem(++curPosition);
                }
            });
            if (index >= mWords.size() - 1) {
                listenFragment.setLastWord(true);
            }
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
                mActionBar.showTitle((position + 1) + "/" + mWords.size());
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
