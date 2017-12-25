package com.qudiandu.smartreader.ui.book.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.book.contract.SRBookUnitsContract;
import com.qudiandu.smartreader.ui.book.view.viewHolder.SRBookUnitsItemVH;
import com.qudiandu.smartreader.ui.dubbing.activity.SRDubbingActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRCatalogue;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/9/7.
 */

public class SRBookUnitsFragment extends ZYBaseFragment<SRBookUnitsContract.IPresenter> implements SRBookUnitsContract.IView {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    ZYBaseRecyclerAdapter<SRCatalogue> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_book_units, container, false);
        ButterKnife.bind(this, view);
        adapter = new ZYBaseRecyclerAdapter<SRCatalogue>() {
            @Override
            public ZYBaseViewHolder<SRCatalogue> createViewHolder(int type) {
                return new SRBookUnitsItemVH();
            }
        };
        adapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SRCatalogue catalogue = adapter.getItem(position);
                if (catalogue != null) {
                    mPresenter.toDubbing(catalogue);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        return view;
    }

    @Override
    public void toDubbing(ArrayList<SRTract> tracts, String bookId, SRCatalogue catalogue) {
        mActivity.startActivity(SRDubbingActivity.createIntent(mActivity, tracts, bookId, catalogue.getCatalogue_id() + "", catalogue.getTitle()));
    }

    @Override
    public void showList(List<SRCatalogue> catalogues) {
        adapter.setDatas(catalogues);
    }
}
