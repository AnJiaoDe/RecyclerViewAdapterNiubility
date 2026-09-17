package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.GridAdapter;


/**
 * Created by cy on 2017/7/2.
 */

public class VerticalGridRecyclerView extends GridRecyclerView<VerticalGridRecyclerView> {

    public VerticalGridRecyclerView(Context context) {
        this(context, null);
    }

    public VerticalGridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(@Nullable final Adapter adapter) {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getSpanCount(), RecyclerView.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // 普通 GridAdapter
                if (adapter instanceof GridAdapter) {
                    GridAdapter<?> gridAdapter = (GridAdapter<?>) adapter;
                    return gridAdapter.isFullSpan(gridAdapter.getItemViewType(position))
                            ? layoutManager.getSpanCount()
                            : 1;
                }
                // MultiAdapter
                if (adapter instanceof ConcatAdapter) {
                    ConcatAdapter concatAdapter = (ConcatAdapter) adapter;
                    int remainPosition = position;
                    for (int i = 0; i < concatAdapter.getAdapters().size(); i++) {
                        GridAdapter<?> gridAdapter = (GridAdapter<?>) concatAdapter.getAdapters().get(i);
                        if (remainPosition < gridAdapter.getItemCount()) {
                            // 找到了当前 position 所属的 GridAdapter
                            return gridAdapter.isFullSpan(gridAdapter.getItemViewType(remainPosition)) ? layoutManager.getSpanCount() : 1;
                        }
                        remainPosition -= gridAdapter.getItemCount();
                    }
                }
                return 1;
            }
        });
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }

    public void setAdapter(@Nullable final Adapter adapter, GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getSpanCount(), RecyclerView.VERTICAL, false);
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }
}
