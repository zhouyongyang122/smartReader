package com.smartreader.base.viewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/16.
 */

public abstract class ZYBaseViewHolder<D> {

    protected Context mContext;
    protected View mItemView;

    public void bindView(View view){
        mItemView = view;
        mContext = view.getContext();
        ButterKnife.bind(this, view);
        findView(view);
    }

    public void findView(View view){

    }

    public View getDataBindingRoot(Context context, ViewGroup parent) {
        return null;
    }

    public abstract void updateView(D data, int position);

    public abstract int getLayoutResId();

    public View getItemView() {
        return mItemView;
    }
}
