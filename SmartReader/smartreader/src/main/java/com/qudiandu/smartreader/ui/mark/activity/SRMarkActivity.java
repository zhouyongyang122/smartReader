package com.qudiandu.smartreader.ui.mark.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.ui.main.model.SRPageManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.presenter.SRMarkPresenter;
import com.qudiandu.smartreader.ui.mark.view.SRMarkFragment;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYWavMergeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkActivity extends ZYBaseFragmentActivity<SRMarkFragment> implements SRMarkFragment.OnTrackClickListener {

    static final String TRACTS = "tracts";
    static final String BOOK_ID = "book_id";

    public static Intent createIntent(Context context, ArrayList<SRTract> tracts, String bookId) {
        Intent intent = new Intent(context, SRMarkActivity.class);
        intent.putExtra(TRACTS, tracts);
        intent.putExtra(BOOK_ID, bookId);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<SRTract> tracts = (ArrayList<SRTract>) getIntent().getSerializableExtra(TRACTS);
        String bookId = getIntent().getStringExtra(BOOK_ID);
        mActionBar.showTitle("(1/" + tracts.size() + ")");
        new SRMarkPresenter(mFragment, tracts, bookId);
        mFragment.setTrackClickListener(this);

        showActionRightTitle("合成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File audio1 = new File(SRApplication.TRACT_AUDIO_ROOT_DIR + "0_1_2");
                File audio2 = new File(SRApplication.TRACT_AUDIO_ROOT_DIR + "0_1_3");
                File audio3 = new File(SRApplication.TRACT_AUDIO_ROOT_DIR + "0_1_4");
                List<File> audios = new ArrayList<File>();
                audios.add(audio1);
                audios.add(audio2);
                audios.add(audio3);
                File outFile = new File(SRApplication.MERGE_AUDIO_ROOT_DIR + "test");
                if (!outFile.exists()) {
                    try {
                        outFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    ZYWavMergeUtils.mergeWav(audios, outFile);
                } catch (IOException e) {
                    ZYLog.e(getClass().getSimpleName(), "merge-error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onTrackClick(int position, int size, SRTract tract) {
        mActionBar.showTitle("(" + (position + 1) + "/" + size + ")");
    }

    @Override
    protected SRMarkFragment createFragment() {
        return new SRMarkFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SRPageManager.getInstance().stopAudio();
    }
}
