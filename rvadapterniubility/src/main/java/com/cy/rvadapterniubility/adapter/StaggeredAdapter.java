package com.cy.rvadapterniubility.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class StaggeredAdapter<T> extends SimpleAdapter<T>{
    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        if (isFullSpan(viewType)) {
            baseViewHolder.setFullSpan(true);
            ((StaggeredGridLayoutManager.LayoutParams) baseViewHolder.itemView.getLayoutParams()).setFullSpan(true);
        }
        return baseViewHolder;
    }

    public boolean isFullSpan(int itemLayoutID) {
        return false;
    }
}
